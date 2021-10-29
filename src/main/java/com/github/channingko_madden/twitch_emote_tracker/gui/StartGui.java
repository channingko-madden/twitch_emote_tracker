package com.github.channingko_madden.twitch_emote_tracker.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.*;

import com.github.channingko_madden.twitch_emote_tracker.EmoteValue;
import com.github.channingko_madden.twitch_emote_tracker.TwitchSocket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class builds the GUI that the user interacts with, in order to start the
 * Twitch emote bot
 * 
 * This GUI allows the user to input the name of a twitch streamer to connect
 * with, and add/remove which emotes to track, then start the bot (which loads a
 * new gui).
 * 
 * @author channing.ko-madden
 *
 */
public class StartGui {
	/** Regex pattern that matches valid Twitch usernames */
	final private Pattern mUsernameRegexPattern = Pattern.compile("^[a-zA-Z0-9_]{4,25}$");
	
	/** JPanel that contains the Start GUI */
	private JPanel mThePanel;

	/** Text field that contains the Twitch channel name to join */
	private JTextField mChannelText;
	/** Text field that user can write the Twitch emote to track */
	private JTextField mAddEmoteText;
	/** Display emotes that are going to be tracked */
	private JList<String> mEmotesList;
	/** The JScrollPane that contains the added emotes */
	private JScrollPane mAddedEmotePane;
	/** Text field for the users Twitch nickname for their twitch account */
	private JTextField mUserTwitchNicknameText;
	/** Text field for the user OAuth token for their twitch account */
	private JTextField mUserOAuthText;
	/** Container that holds the added emotes */
	private LinkedHashSet<String> mListData = new LinkedHashSet<>();

	/**
	 * Default constructor
	 * @param launchListener Listener to the GUI element that is used to signal the app to launch
	 */
	public StartGui(ActionListener launchListener) {
		buildLaunchGui(launchListener);
	}

	/**
	 * Build the User GUI.
	 * 
	 * GUI uses a BorderLayout to organize the components of the GUI
	 * @param launchListener Listener to the GUI element that is used to signal the app to launch
	 */
	private void buildLaunchGui(ActionListener launchListener) {
		mThePanel = new JPanel(new BorderLayout());
		// empty border gives a margin between edges of the panel and where the
		// components are placed. Aka it looks nice.
		mThePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Text field for inputting the twitch channel to connect with
		mChannelText = new JTextField("Twitch channel name", 25); // twitch channel name can be at max 25 characters
		mChannelText.addActionListener(new ChannelNameChecker());
		mThePanel.add(BorderLayout.NORTH, mChannelText);

		// Create Box with horizontally organized components for adding/removing emotes to track.
		Box emoteChoiceBox = new Box(BoxLayout.X_AXIS);
		emoteChoiceBox.setBorder(null); // no border for this box

		mAddEmoteText = new JTextField(25);
		mAddEmoteText.setText("Kappa");
		mAddEmoteText.addActionListener(new NewEmoteTextListener());
		emoteChoiceBox.add(mAddEmoteText);

		Box emoteButtonBox = new Box(BoxLayout.Y_AXIS); // organize add/remote buttons vertically

		JButton addButton = new JButton("Add");
		addButton.addActionListener(new AddButtonListener());
		emoteButtonBox.add(addButton);

		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new RemoveButtonListener());
		emoteButtonBox.add(removeButton);

		emoteChoiceBox.add(emoteButtonBox);

		// Holds added emotes to track
		mEmotesList = new JList<String>();
		mEmotesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		mAddedEmotePane = new JScrollPane(mEmotesList); // scroll pane so user can scroll and see all the emotes
		mAddedEmotePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		mAddedEmotePane.setViewportBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		emoteChoiceBox.add(mAddedEmotePane);

		mThePanel.add(BorderLayout.CENTER, emoteChoiceBox);

		// Create Box with horizontally organized components for launching the twitch bot
		Box launchBox = new Box(BoxLayout.X_AXIS);
		launchBox.setBorder(null); // no border for this box

		Box userAuthBox = new Box(BoxLayout.Y_AXIS); // organize text fields where user can input their Twitch nickname and Outh token
		
		mUserTwitchNicknameText = new JTextField(25);
		mUserTwitchNicknameText.addActionListener(new ChannelNameChecker());
		mUserTwitchNicknameText.setText("Insert your Twitch nickname");
		userAuthBox.add(mUserTwitchNicknameText);
		
		mUserOAuthText = new JTextField();
		mUserOAuthText.setText("Insert your OAuth token");
		userAuthBox.add(mUserOAuthText);
		
		launchBox.add(userAuthBox);

		JButton launchButton = new JButton("Launch");
		launchButton.addActionListener(launchListener);
		launchBox.add(launchButton);

		mThePanel.add(BorderLayout.SOUTH, launchBox);
	}
	
	/**
	 * Return a JPanel that contains the GUI, for displaying by the main class
	 * @return JPanel with GUI set up
	 */
	public JPanel getPanel() {
		return mThePanel;
	}
	
	/**
	 * Return a list of the emotes to track
	 * @return List of emotes to track
	 */
	public List<EmoteValue> getTargetEmotes() {
		String[] trackEmotes = mListData.toArray(new String[mListData.size()]);
		EmoteValue[] emoteValues = new EmoteValue[trackEmotes.length];
		for (int i = 0; i < emoteValues.length; i++) {
			emoteValues[i] = new EmoteValue(trackEmotes[i]);
		}
		return Arrays.asList(emoteValues);
	}
	
	/** 
	 * Return the name of the Twitch Channel to join
	 * @return Twitch Channel Name
	 */
	public String getChannelName() {
		return mChannelText.getText();
	}
	
	/**
	 * Return the user's Twitch Account name
	 * @return User's Twitch Account name
	 */
	public String getUserName() {
		return mUserTwitchNicknameText.getText();
	}
	
	/**
	 * Return the user's OAuth token
	 * @return User's OAuth token
	 */
	public String getOAuth() {
		return mUserOAuthText.getText();
	}
	
	/**
	 * Return if the user has input data in the GUI correctly, and the app is ready to launch
	 * @return True if app is launchable, false otherwise
	 */
	public boolean launchable() {
		boolean launchable = validUsername(mChannelText);
		launchable &= validUsername(mUserTwitchNicknameText);
		launchable &= validOAuth(mUserOAuthText.getText());
		launchable &= addedEmoteCheck();
		return launchable;
	}

	/**
	 * Return if a username is a valid Twitch username
	 * 
	 * @param username Username to check
	 * @return True if username is valid, false otherwise
	 */
	private boolean checkUsername(String username) {
		if (username != null && !username.isEmpty()) {
			final Matcher matcher = mUsernameRegexPattern.matcher(username);
			if (matcher.results().count() == 1) // should only be one match
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return if the users OAuth token is valid  
	 * 
	 * Checks include:
	 * <ul>
	 * 	<li> A non-empty token was input </li>
	 * </ul>
	 * 
	 * @param username Username to check
	 * @return True if username is valid, false otherwise
	 */
	private boolean validOAuth(String token) {
		if (token != null && !token.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check if input Channel name is valid, and if not warn user by setting text
	 * color to red.
	 * 
	 * @param textField JTextField object that contains the channel name text
	 * @return True if username is valid, false otherwise
	 */
	private boolean validUsername(JTextField textField) {
		if (!checkUsername(textField.getText())) {
			textField.setSelectedTextColor(Color.RED);
			textField.setForeground(Color.RED);
			return false;
		} else {
			textField.setSelectedTextColor(Color.BLACK);
			textField.setForeground(Color.BLACK);
			return true;
		}
	}
	
	/**
	 * Check if there are emotes added to be tracked.
	 * 
	 * If no emotes have been added, a Red border is placed around the Viewport of the JScrollPane.
	 * If emotes have been, the Viewport Border is removed
	 * @return True if emotes have been added, false otherwise.
	 */
	private boolean addedEmoteCheck() {
		if (mListData.isEmpty()) {
			mEmotesList.setBorder(BorderFactory.createLineBorder(Color.RED));
			return false;
		} else {
			mEmotesList.setBorder(BorderFactory.createEmptyBorder());
			return true;
		}
	}

	/**
	 * This class checks if a channel name input by the user can be a possible
	 * twitch name, and warns the user of incorrect names by changing the text color
	 * to red.
	 * 
	 * @author channing.ko-madden
	 *
	 */
	private class ChannelNameChecker implements ActionListener {

		/**
		 * Check if channel name is a valid twitch channel name
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JTextField) {
				validUsername((JTextField) e.getSource());
			}
		}

	}

	/**
	 * This class listens to the "Add" emote button, and moves the String contained
	 * within mAddEmoteText into the mEmotesList
	 * 
	 * @author channing.ko-madden
	 *
	 */
	private class AddButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// mAddEmoteText.selectAll();
			// final String newEmote = mAddEmoteText.getSelectedText();
			final String newEmote = mAddEmoteText.getText();
			if (newEmote == null) {
				System.out.println("emote string is null");
			} else if (newEmote.isEmpty()) {
				System.out.println("emote string is empty");
			} else if (mListData.contains(newEmote)) {
				System.out.println("emote is already added");
			} else {
				mListData.add(newEmote);
				final DefaultListModel<String> listModel = new DefaultListModel<String>();
				for (String emote : mListData) {
					listModel.addElement(emote);
				}
				mEmotesList.setModel(listModel);
				mEmotesList.setBorder(BorderFactory.createEmptyBorder());
			}
			mAddEmoteText.setText("");
		}

	}

	/**
	 * This class listens to the "Remove" emote button, and removes the component
	 * the user has selected within the mEmotesList
	 * 
	 * @author channing.ko-madden
	 *
	 */
	private class RemoveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final List<String> removeList = mEmotesList.getSelectedValuesList();
			for (String item : removeList) {
				mListData.remove(item);
			}

			mEmotesList.clearSelection();
			final DefaultListModel<String> listModel = new DefaultListModel<String>();
			for (String emote : mListData) {
				listModel.addElement(emote);
			}
			mEmotesList.setModel(listModel);
		}

	}


	/**
	 * This class checks when the new emote text field is edited. Currently used for
	 * debugging.
	 * 
	 * @author channing.ko-madden
	 *
	 */
	private class NewEmoteTextListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(mAddEmoteText.getText());
		}

	}

}
