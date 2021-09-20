package com.github.channingko_madden.twitch_kekw_chatbot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class builds the GUI that the user interacts with, in order to start the Twitch emote bot
 * 
 * This GUI allows the user to input the name of a twitch streamer to connect with, and add/remove which emotes to track,
 * then start the bot (which loads a new gui).
 * @author channing.ko-madden
 *
 */
public class StartGui 
{
	/** Regex pattern that matches valid Twitch usernames */
	final private Pattern mUsernameRegexPattern = Pattern.compile("^[a-zA-Z0-9_]{4,25}$");

	/** The main GUI frame */
	private JFrame mTheFrame = new JFrame("Twitch emote bot");
	/** Text field that contains the Twitch channel name to join */
	private JTextField mChannelText;
	/** Text field that user can write the Twitch emote to track */
	private JTextField mAddEmoteText;
	/** Display emotes that are going to be tracked */
	private JList<String> mEmotesList;
	/** Container that holds the added emotes */
	private LinkedHashSet<String> mListData = new LinkedHashSet<>();
	
	/**
	 * Default constructor
	 */
	public StartGui()
	{
		buildLaunchGui();
	}
	
	/**
	 * Build the User GUI.
	 * 
	 * GUI uses a BorderLayout to organize the components of the GUI
	 */
	private void buildLaunchGui()
	{
		mTheFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel background = new JPanel(new BorderLayout());
		// empty border gives a margin between edges of the panel and where the components are placed. Aka it looks nice.
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Text field for inputting the twitch channel to connect with
		mChannelText = new JTextField("Twitch channel name", 25); // twitch channel name can be at max 25 characters
		mChannelText.addActionListener(new ChannelNameChecker());
		background.add(BorderLayout.NORTH, mChannelText);
		
		// Create Box with horizontally organized components for adding/removing emotes to track.
		Box emoteChoiceBox = new Box(BoxLayout.X_AXIS);
		emoteChoiceBox.setBorder(null); // no border for this box
		
		mAddEmoteText = new JTextField(25);
		mAddEmoteText.setText("kappa");
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

		JScrollPane theList = new JScrollPane(mEmotesList); // scroll pane so user can scroll and see all the emotes
		theList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		emoteChoiceBox.add(theList);
		
		background.add(BorderLayout.CENTER, emoteChoiceBox);
		
		JButton launchButton = new JButton("Launch");
		launchButton.addActionListener(new LaunchButtonListener());
		
		background.add(BorderLayout.SOUTH, launchButton);
		
		mTheFrame.getContentPane().add(background);
		mTheFrame.setBounds(50, 50, 300, 300);
		mTheFrame.pack();
		mTheFrame.setVisible(true);
	}
	
	/**
	 * Return if a username is a valid Twitch username
	 * @param username Username to check
	 * @return True if username is valid, false otherwise
	 */
	private boolean checkUsername(String username)
	{
		if (username != null && !username.isEmpty())
		{
			final Matcher matcher = mUsernameRegexPattern.matcher(username);
			if (matcher.results().count() == 1) // should only be one match
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if input Channel name is valid, and if not warn user by setting text color to red.
	 * @return True if username is valid, false otherwise
	 */
	private boolean validUsername()
	{
		if (!checkUsername(mChannelText.getText()))
		{
			mChannelText.setSelectedTextColor(Color.RED);
			mChannelText.setForeground(Color.RED);
			return false;
		}
		else
		{
			mChannelText.setSelectedTextColor(Color.BLACK);
			mChannelText.setForeground(Color.BLACK);
			return true;
		}
	}
	
	
	/**
	 * This class checks if a channel name input by the user can be a possible twitch name, and warns the user of incorrect names by changing the text
	 * color to red.
	 * @author channing.ko-madden
	 *
	 */
	private class ChannelNameChecker implements ActionListener
	{

		/**
		 * Check if channel name is a valid twitch channel name
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			validUsername();
		}
		
	}
	
	/**
	 * This class listens to the "Add" emote button, and moves the String contained within mAddEmoteText into the mEmotesList
	 * @author channing.ko-madden
	 *
	 */
	private class AddButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			//mAddEmoteText.selectAll();
			//final String newEmote = mAddEmoteText.getSelectedText();
			final String newEmote = mAddEmoteText.getText();
			if (newEmote == null)
			{
				System.out.println("emote string is null");
			}
			else if (newEmote.isEmpty())
			{
				System.out.println("emote string is empty");
			}
			else if (mListData.contains(newEmote))
			{
				System.out.println("emote is already added");
			}
			else
			{
				mListData.add(newEmote);
				final DefaultListModel<String> listModel = new DefaultListModel<String>();
				for (String emote : mListData)
				{
					listModel.addElement(emote);
				}
				mEmotesList.setModel(listModel);
			}
			
			mAddEmoteText.setText("");
		}
		
	}
	
	/**
	 * This class listens to the "Remove" emote button, and removes the component the user has selected within the mEmotesList
	 * @author channing.ko-madden
	 *
	 */
	private class RemoveButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			final List<String> removeList = mEmotesList.getSelectedValuesList();
			for (String item : removeList)
			{
				mListData.remove(item);
			}
			
			mEmotesList.clearSelection();
			final DefaultListModel<String> listModel = new DefaultListModel<String>();
			for (String emote : mListData)
			{
				listModel.addElement(emote);
			}
			mEmotesList.setModel(listModel);
		}
		
	}
	
	/**
	 * This class listens to the "Launch" button. When pressed, the Twitch bot will join the channel and begin tracking the emotes.
	 * The Running GUI is displayed afterwards.
	 * @author channing.ko-madden
	 *
	 */
	private class LaunchButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (validUsername() && !mListData.isEmpty())
			{
				System.out.println("Launching twitch bot");
			}
		}
	}
	

	/**
	 * This class checks when the new emote text field is edited. Currently used for debugging.
	 * @author channing.ko-madden
	 *
	 */
	private class NewEmoteTextListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(mAddEmoteText.getText());
		}
		
	}

}
