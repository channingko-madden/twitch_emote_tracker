package com.github.channingko_madden.twitch_emote_tracker.gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.github.channingko_madden.twitch_emote_tracker.EmoteValue;

/**
 * Table Model for displaying Emote data
 * @author channing.ko-madden
 *
 */
public class EmoteTableModel extends AbstractTableModel{
	
	private final List<EmoteValue> mEmoteList;
	private final String[] columnNames = {"Emote", "Count", "Queries"};
	
	public EmoteTableModel(List<EmoteValue> emotes) {
		mEmoteList = emotes;
	}

	@Override
	public int getRowCount() {
		return mEmoteList.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return mEmoteList.get(rowIndex).string();
		} else if (columnIndex == 1) {
			return mEmoteList.get(rowIndex).getCount();
		} else if (columnIndex == 2) {
			return mEmoteList.get(rowIndex).getQueries();
		} else {
			return "";
		}
	}

}
