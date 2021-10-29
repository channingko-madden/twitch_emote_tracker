package com.github.channingko_madden.twitch_emote_tracker.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import com.github.channingko_madden.twitch_emote_tracker.EmoteValue;

/**
 * This class builds the GUI that displays to the user the data it is tracking.
 * 
 * This GUI allows the user to return to the start GUI, with or without saving the data gathered 
 * by this point.
 * 
 * @author channing.ko-madden
 *
 */
public class RunningGui {
	
	private JPanel mThePanel;
	
	/**
	 * Constructor 
	 * @param theFrame The JFrame for this app.
	 */
	public RunningGui() {

		mThePanel = new JPanel(new BorderLayout());
		// empty border gives a margin between edges of the panel and where the
		// components are placed. Aka it looks nice.
		mThePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}
	
	/**
	 * Build the GUI based on the emotes to track
	 * @param emotes Emotes to display data of
	 * @param closeListener Listener for the close button
	 */
	public void buildGui(List<EmoteValue> emotes, ActionListener closeListener) {

		mThePanel.removeAll();
		
		// create table and place inside a scrolling pane
		JTable dataTable = new JTable(new EmoteTableModel(emotes));
		dataTable.setFillsViewportHeight(true);
		
		JScrollPane scrollPane = new JScrollPane(dataTable);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		mThePanel.add(BorderLayout.CENTER, scrollPane);
		
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(closeListener);
		
		mThePanel.add(BorderLayout.SOUTH, closeButton);
		
	}
	
	/**
	 * Return a JPanel that contains the GUI, for displaying by the main class
	 * @return JPanel with GUI set up
	 */
	public JPanel getPanel() {
		return mThePanel;
	}
	

}
