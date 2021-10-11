package com.github.channingko_madden.twitch_kekw_chatbot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class opens a socket connection to the twitch irc, attempts to join a
 * channel, and listens to chat, counting how many times "KEKW" is posted in
 * chat.
 * 
 * Chat users can post the "!kekws" command, and the number of recoded KEKWs
 * will be sent back to the chat.
 *
 */
public class TwitchSocket {

	final private String server = "irc.chat.twitch.tv";
	final private int port = 6667;
	final private String mNickname;
	final private String mToken;
	final private String mChannelName;
	private Socket mChatSocket;
	private BufferedReader mChatReader;
	private BufferedWriter mTwitchWriter; // Had success with buffered write, but don't know why
	final private Lock mMessageLock = new ReentrantLock();
	final private Condition mMessageCond = mMessageLock.newCondition();
	final private ArrayList<String> mMessageList = new ArrayList<>();
	final private List<EmoteValue> mEmoteValues;
	/** Flag to flip when a unique chat message is created, so the next one created is also unique. */
	private boolean mNeedUniqueMsg = false; 

	/**
	 * Constructor
	 * 
	 * @param channel_name Twitch channel to join (ex. t90official)
	 * @param nickname     Your twich account name
	 * @param token        OAUTH token for your twitch account
	 * @param emotes The emotes the user wants to track
	 */
	public TwitchSocket(String channel_name, String nickname, String token, List<EmoteValue> emotes) {
		this.mChannelName = "#" + channel_name;
		this.mNickname = nickname;
		this.mToken = token;
		mEmoteValues = emotes;
		setUpNetworking();
		Thread readerThread = new Thread(new ReadThread());
		readerThread.start();
		Thread processThread = new Thread(new ProcessThread());
		processThread.start();
		setUpTwitch();
	}

	/**
	 * Set up Socket that connects to the twitch IRC and create reader/writers from
	 * socket
	 */
	private void setUpNetworking() {
		try {
			mChatSocket = new Socket(server, port);
			mChatReader = new BufferedReader(new InputStreamReader(mChatSocket.getInputStream()));
			mTwitchWriter = new BufferedWriter(
					new OutputStreamWriter(mChatSocket.getOutputStream(), StandardCharsets.UTF_8));
			System.out.println("Networking established");
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}

	/**
	 * Configure Token, Nickname, and Channel to join with Twitch IRC
	 */
	private void setUpTwitch() {
		try {
			mTwitchWriter.write("PASS " + mToken + "\r\n"); // send our token
			mTwitchWriter.flush();
			mTwitchWriter.write("NICK " + mNickname + "\r\n"); // send our nickname
			mTwitchWriter.flush();
			mTwitchWriter.write("JOIN " + mChannelName + "\r\n"); // send what channel to listen to
			mTwitchWriter.flush();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	/**
	 * Reads from the Twitch IRC and prints out the messages
	 */
	private class ReadThread implements Runnable {

		@Override
		public void run() {

			String message;
			try {
				while ((message = mChatReader.readLine()) != null) {
					// System.out.println("MESSAGE: " + message);
					try {
						mMessageLock.lock();
						mMessageList.add(message);
						mMessageCond.signal();
						mMessageLock.unlock();
					} catch (IllegalMonitorStateException exp) {
						exp.printStackTrace();
					}
				}
			} catch (Exception exp) {
				exp.printStackTrace();
			} finally {
				System.out.println("Read thread over");
			}

		}

	}

	/**
	 * Extracts messages from the mMessageList and searches if it has KEKW in it.
	 * Waits on a condition variable until messages are within the mMessageList.
	 */
	private class ProcessThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				mMessageLock.lock();
				if (mMessageList.isEmpty()) {
					try {
						mMessageCond.await();
					} catch (Exception exp) {
						exp.printStackTrace();
					}
				}

				if (!mMessageList.isEmpty()) {
					String msg = mMessageList.get(0);
					mMessageList.remove(0);
					try {
						mMessageLock.unlock();
					} catch (IllegalMonitorStateException exp) {
						exp.printStackTrace();
					}
					// process messages here
					processMsg(msg);
				} else {
					try {
						mMessageLock.unlock(); // make sure to always unlock the lock!
					} catch (IllegalMonitorStateException exp) {
						exp.printStackTrace();
					}
				}
			}
		}

	}

	/**
	 * Search for "KEKW" within a message body and increment the internal KEKW
	 * counter if found
	 * 
	 * Handles PING/PONG protocol
	 * 
	 * Handles the following commands "!kekws"
	 * 
	 * @param message Chat message from Twitch
	 */
	private void processMsg(final String message) {
		if (message.startsWith("PING")) {
			try {
				System.out.println("PING Received: " + message);
				mTwitchWriter.write("PONG " + message.substring(message.indexOf("PING") + "PING".length()) + "\r\n"); // ex reply. PONG :tmi.twitch.tv
				mTwitchWriter.flush();
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		} else {
			// Want to parse PRIVMSG only for emotes. Format looks like:
			// twitch_lurker:!twitch_lurker@twitch_lurker.tmi.twitch.tv PRIVMSG
			// #channel_name :EZ Clap!
			final int privmsg_pos = message.indexOf("PRIVMSG");
			if (privmsg_pos != -1) {
				final int colon_pos = message.indexOf(':', privmsg_pos + "PRIVMSG".length());
				if (colon_pos != -1) // might not be necessary
				{
					// System.out.println(message.substring(colon_pos + 1));
					if (message.substring(colon_pos + 1).startsWith("!emotecount ")) {
						System.out.println("!emotecount command received");
						String countCall = message.substring(colon_pos + "!emotecount ".length()).trim();
						for (EmoteValue emote : mEmoteValues) {
							if (emote.string().equals(countCall)) {
								sendChatMessage(createUniqueMessage(emote.getCount() + " " + emote.string(), "... ;p"));
								return;
							}
						}
						sendChatMessage(createUniqueMessage(":( " + countCall + " is not being tracked", ":("));
					} else {
						for (EmoteValue emote : mEmoteValues) {
							if (emote.search(message.substring(colon_pos + 1))) {
								System.out.println("New " + emote.string() + " Count: " + emote.getCount());
							}
						}
					}
				}
			} else {
				System.out.println(message); // print out non PRIVMSG messages
			}
		}
	}
	
	/**
	 * Combine the message and emote to create a unique message, so that Twitch does not ignore repeated identical messages.
	 * 
	 * @param message Primary message to send
	 * @param emote Emote that is optionally added to message if needed to make it a unique message
	 * @return Unique message
	 */
	private String createUniqueMessage(final String message, final String emote)
	{
		if (mNeedUniqueMsg) {
			mNeedUniqueMsg = false;
			return message + " " + emote;
		} else {
			mNeedUniqueMsg = true;
			return message;
		}
	}
	
	/**
	 * Send a PRIVMSG (aka chat message) to the currently joined channel
	 * 
	 * @param message Chat message
	 */
	private void sendChatMessage(final String message) {
		try {
			mTwitchWriter.write("PRIVMSG " + mChannelName + " :" + message + "\r\n"); // send Private message
			mTwitchWriter.flush();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

}
