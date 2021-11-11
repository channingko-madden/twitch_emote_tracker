package com.github.channingko_madden.twitch_emote_tracker.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;

import com.github.channingko_madden.twitch_emote_tracker.EmoteEvent;
import com.github.channingko_madden.twitch_emote_tracker.EmoteSubscriber;
import com.github.channingko_madden.twitch_emote_tracker.EmoteValue;

/**
 * This class builds the GUI that displays to the user the data it is tracking.
 * 
 * This GUI allows the user to return to the start GUI.
 * In the future there will be a prompt before returning asking the user if they want to return with or without saving the data gathered 
 * up to this point.
 * 
 * @author channing.ko-madden
 *
 */
public class RunningGui {
	
	/** JPanel that contains the Running GUI */
	private JPanel mThePanel;
	/** Table that displays emote statistics */
	private JTable mDataTable;
	
	/**
	 * Constructor 
	 * @param theFrame The JFrame for this app.
	 */
	public RunningGui() {

		mThePanel = new JPanel(new BorderLayout());
		// empty border gives a margin between edges of the panel and where the components are placed. Aka it looks nice.
		mThePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}
	
	/**
	 * Build the GUI based on the emotes to track
	 * @param emotes Emotes to display data of
	 * @param closeListener Listener for the close button
	 */
	public void buildGui(List<EmoteValue> emotes, ActionListener closeListener) {

		// Clear the panel so we can rebuild it.
		mThePanel.removeAll();
		
		// create table and place inside a scrolling pane
		mDataTable = new JTable(new EmoteTableModel(emotes));
		mDataTable.setFillsViewportHeight(true);
		
		for (int i = 0; i < emotes.size(); i++) {
			emotes.get(i).addEmoteListener(new EmoteRowListener(i));
		}
		
		JScrollPane scrollPane = new JScrollPane(mDataTable);
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
	
	/**
	 * This class listens to emote events for a given emote, and tells the data table to update the cell that displays the data
	 * @author channing.ko-madden
	 *
	 */
	private class EmoteRowListener implements EmoteSubscriber {
		
		private int row;
		
		/**
		 * Constructor
		 * @param row Row of JTable that emote data is displayed at
		 */
		public EmoteRowListener(int row) {
			this.row = row;
		}

		@Override
		public void actionPerformed(EmoteEvent event) {
			switch (event.getType()) {
				case Count: {
					AbstractTableModel model = (AbstractTableModel) mDataTable.getModel();
					model.fireTableCellUpdated(row, EmoteTableModel.TableColumns.get(EmoteEvent.Type.Count));
					break;
				} case Query: {
					AbstractTableModel model = (AbstractTableModel) mDataTable.getModel();
					model.fireTableCellUpdated(row, EmoteTableModel.TableColumns.get(EmoteEvent.Type.Query));
					break;
				} default: {
					break;
				}
			}
		}
	}
}
