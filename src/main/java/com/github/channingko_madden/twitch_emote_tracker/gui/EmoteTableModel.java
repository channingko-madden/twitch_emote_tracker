package com.github.channingko_madden.twitch_emote_tracker.gui;

import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.github.channingko_madden.twitch_emote_tracker.EmoteEvent;
import com.github.channingko_madden.twitch_emote_tracker.EmoteValue;

/**
 * Table Model for displaying Emote data
 * @author channing.ko-madden
 *
 */
public class EmoteTableModel extends AbstractTableModel{

	/**
	 * Mappings of EmoteEvent types to table column numbers, so that an EmoteSubscriber knows
	 * what columns need to be updated based on what data has changed.
	 */
	public static Map<EmoteEvent.Type, Integer> TableColumns = 
			Map.of(EmoteEvent.Type.Count, 1, EmoteEvent.Type.Query, 2);
	
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
	public String getColumnName(int column) {
		return columnNames[column];
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
