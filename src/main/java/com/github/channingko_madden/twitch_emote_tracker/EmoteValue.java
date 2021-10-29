package com.github.channingko_madden.twitch_emote_tracker;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * This class can be used to count the occurrences of an emote within a String, and 
 * stores a running total of occurrences.
 *
 */
public class EmoteValue implements EmotePublisher {
	
	/** The string a Twitch user types in chat for an emote */
	private final String mEmoteString;
	/** Regex pattern for finding the occurrences of the emote within a String */
	private final Pattern mEmoteRegexPattern; 	
	/** Store occurrences of the emote */
	private long mEmoteCount = 0;
	/** Store the times this emote is queried for by chat users */
	private long mQueries = 0;
	private ArrayList<EmoteSubscriber> mSubscribers = new ArrayList<EmoteSubscriber>();

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
	 * @return The number of times this emote has been queried for.
	 */
	public long getQueries() {
		return mQueries;
	}

	
	/**
	 * @return Increment the number of queries for this emote that have occured
	 */
	public void incrementQueries() {
		mQueries++;
		publishEvent(new MyEvent(EmoteEvent.Type.Query));
	}
	 
	 /**
	  * Clear the running count of occurrences of the emote and queries for the emote
	  */
	 public void clear() {
		 mEmoteCount = 0;
		 mQueries = 0;
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
			publishEvent(new MyEvent(EmoteEvent.Type.Count));
			return true;
		}
	}

	@Override
	public void addEmoteListener(EmoteSubscriber subscriber) {
		mSubscribers.add(subscriber);
	}

	@Override
	public void removeEmoteListener(EmoteSubscriber subscriber) {
		mSubscribers.remove(subscriber);
	}
	
	/**
	 * Publish an event to all subscribers
	 * @param event Event to publish
	 */
	private void publishEvent(EmoteEvent event) {
		for (EmoteSubscriber sub : mSubscribers) {
			sub.actionPerformed(event);
		}
	}
	
	/**
	 * This class is used to pass event data to subscribers
	 * @author channing.ko-madden
	 *
	 */
	private class MyEvent implements EmoteEvent {
		
		/** Define what type of event this is */
		private final Type type;
		
		public MyEvent(Type type) {
			this.type = type;
		}
		
		@Override
		public String getEmoteText() {
			return mEmoteString;
		}

		@Override
		public long getEmoteCount() {
			return mEmoteCount;
		}

		@Override
		public long getEmoteQueries() {
			return mQueries;
		}

		@Override
		public Type getType() {
			return this.type;
		}
		
	}

}
