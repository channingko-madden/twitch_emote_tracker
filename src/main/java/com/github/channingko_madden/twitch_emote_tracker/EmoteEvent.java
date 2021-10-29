package com.github.channingko_madden.twitch_emote_tracker;

/**
 * This interface defines an event for when a tracked emote is parsed from the chat
 */
public interface EmoteEvent {
	
	/**
	 * This enum denotes the type of event that has occurred
	 * Count - Emote count has changed
	 * Query - The number of emote queries has changed
	 * @author channing.ko-madden
	 *
	 */
	public enum Type {
		Count,
		Query
	}
	
	/**
	 * Return the type of event that has occurred
	 * @return
	 */
	Type getType();
	
	/**
	 * Return the text representation of the emote
	 * @return Emote text
	 */
	String getEmoteText();
	
	/**
	 * Return the emote count
	 * @return Emote count
	 */
	long getEmoteCount();
	
	/**
	 * Return the number of emote queries
	 * @return Emote queries
	 */
	long getEmoteQueries();

}
