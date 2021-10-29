package com.github.channingko_madden.twitch_emote_tracker;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.channingko_madden.twitch_emote_tracker.gui.RunningGui;
import com.github.channingko_madden.twitch_emote_tracker.gui.StartGui;

/*
 * Main class
 * 
 * Wants three arguments passed
 * 	1. Name of twitch channel you want to join (ex. ninja, liihs)
 *  2. Twitch account username to be used
 *  3. OAuth token generated for the twitch account
 */
public class HelloTwitch {

	public static void main(String[] args) {

		System.out.println("Hello Twitch");

		HelloTwitch gui = new HelloTwitch();
		// StartGui gui = new StartGui();
		// final TwitchSocket testSocket = new TwitchSocket(args[0], args[1], args[2]);
	}
	
	/** The main GUI frame */
	private JFrame mTheFrame = new JFrame("Twitch emote bot");
	private StartGui mStartGui;
	private final String STARTPANEL = "Start Panel";
	private final String RUNNINGPANEL = "Running Panel";
	private JPanel mCards;
	private TwitchSocket mTwitchSocket;
	private RunningGui mRunningGui;
	
	public HelloTwitch() {
		buildGui();
	}
	
	private void buildGui() {
		
		mTheFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mCards = new JPanel(new CardLayout());
		mStartGui = new StartGui(new LaunchListener());
		mCards.add(mStartGui.getPanel(), STARTPANEL);
		mRunningGui = new RunningGui();
		mCards.add(mRunningGui.getPanel(), RUNNINGPANEL);
		
		mTheFrame.getContentPane().add(mCards);
		mTheFrame.setBounds(50, 50, 300, 300);
		mTheFrame.pack();
		mTheFrame.setVisible(true);
		
	}
	
	
	/**
	 * This class listens to the "Launch" button. When pressed, the Twitch bot will
	 * join the channel and begin tracking the emotes. The Running GUI is displayed
	 * afterwards.
	 * 
	 * @author channing.ko-madden
	 *
	 */
	private class LaunchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (mStartGui.launchable()) {
				System.out.println("Launching bot");
				List<EmoteValue> emotes = mStartGui.getTargetEmotes();
				mRunningGui.buildGui(emotes, new CloseListener());
				
				mTwitchSocket = new TwitchSocket(
						mStartGui.getChannelName(),
						mStartGui.getUserName(),
						mStartGui.getOAuth(),
						emotes);
				
				CardLayout layout = (CardLayout) (mCards.getLayout());
				layout.show(mCards, RUNNINGPANEL);
			}
		}
	}
	
	/**
	 * This class listens to the "Close" button. When pressed, the Twitch bot is stopped, and the Running GUI destroyed and replaced
	 * with the Start GUI
	 * @author channing.ko-madden
	 *
	 */
	private class CloseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			mTwitchSocket.close();
			CardLayout layout = (CardLayout) (mCards.getLayout());
			layout.show(mCards, STARTPANEL);
		}
		
	}
	
}
