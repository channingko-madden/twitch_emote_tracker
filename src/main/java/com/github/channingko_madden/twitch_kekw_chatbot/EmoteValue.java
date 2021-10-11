package com.github.channingko_madden.twitch_kekw_chatbot;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This class can be used to count the occurrences of an emote within a String, and 
 * stores a running total of occurrences.
 *
 */
public class EmoteValue {
	
	/** The string a Twitch user types in chat for an emote */
	private final String mEmoteString;
	/** Regex pattern for finding the occurrences of the emote within a String */
	private final Pattern mEmoteRegexPattern; 	
	/** Store occurrences of the emote */
	private long mEmoteCount = 0;

	/**
	 * Constructor
	 * @param twitchEmote String for a Twitch emote
	 */
	public EmoteValue(String twitchEmote) {
		mEmoteString = twitchEmote;
		mEmoteRegexPattern = Pattern.compile("^" + twitchEmote + "(?=([\\s]|$))|(?<=[\\s])" + twitchEmote + "(?=[\\s])|(?<=[\\s])" + twitchEmote + "$");
	}

	/**
	 * @return The String for this emote
	 */
	public String string() {
		return mEmoteString;
	}
	
	/**
	 * @return The running count of occurrences of the emote
	 */
	public long getCount() {
		return mEmoteCount;
	}
	 
	 /**
	  * Clear the running count of occurrences of the emote
	  */
	 public void clear() {
		 mEmoteCount = 0;
	 }
	
	/**
	 * Search the message for occurrences of the emote, and return if any occurrences are found.
	 * 
	 * Uses REGEX Pattern to find the number of occurrences, and this number is added to the running count.
	 * 
	 * @param message
	 * @return True if at least one occurrence of the emote is within the message, false otherwise
	 */
	public boolean search(final String message) {
		Matcher matcher = mEmoteRegexPattern.matcher(message);
		long count = matcher.results().count(); 
		if (count == 0) {
			return false;
		} else {
			mEmoteCount += count;
			return true;
		}
	}

}
