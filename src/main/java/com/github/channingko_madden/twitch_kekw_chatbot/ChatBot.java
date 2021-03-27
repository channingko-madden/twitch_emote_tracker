package com.github.channingko_madden.twitch_kekw_chatbot;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

/**
 * Attempt at implementing chatbot with twitch4j library.
 * Decided to implement only what I needed in TwitchSocket class, so this class is unfinished.
 * @author channing.ko-madden
 *
 */
public class ChatBot
{
	private OAuth2Credential mCredentials;
	private TwitchClient mTwitchClient;
	private ChatEventListener mChatListener = new ChatEventListener();

	/**
	 * 
	 * @param channel_name Channel name to join
	 * @param token OAuth token for your twitch account
	 */
	public ChatBot(String channel_name, String token)
	{
		mCredentials = new OAuth2Credential("twitch", token);
		mTwitchClient = TwitchClientBuilder.builder()
				.withEnableChat(true)
				.withChatAccount(mCredentials)
				.build();
		mTwitchClient.getChat().connect();
		mTwitchClient.getChat().joinChannel(channel_name);
		
		/*
		mTwitchClient.getEventManager().onEvent(null, null).subscribe(event -> {
			System.out.println("[" + event.getChannel().getName() + "] " + event.getUser().getName() + ": " + event.getMessage());
		});
		*/
		mTwitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelMessageEvent.class, mChatListener::onMessageEvent);
		
	}
	
	/**
	 * Listener class for chat events 
	 */
	private class ChatEventListener
	{
		private void onMessageEvent(ChannelMessageEvent event)
		{
			System.out.println("[" + event.getChannel().getName() + "]" + event.getUser().getName() + ": " + event.getMessage());
		}
		
	}

}
