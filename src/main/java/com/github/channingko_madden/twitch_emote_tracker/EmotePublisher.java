package com.github.channingko_madden.twitch_emote_tracker;

/**
 * This interface defines a publisher of emote events
 */
public interface EmotePublisher {
	
	/**
	 * Add a subscriber of this emote publisher
	 * @param subscriber Subscriber to add
	 */
	void addEmoteListener(EmoteSubscriber subscriber);
	
	/**
	 * Remove a subscriber from this publisher
	 * @param subscriber Subscriber to remove
	 */
	void removeEmoteListener(EmoteSubscriber subscriber);

}
