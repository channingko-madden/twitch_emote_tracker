package com.github.channingko_madden.twitch_emote_tracker;

/**
 * This interface defines a subscriber of emote events
 */
public interface EmoteSubscriber {
	
	/**
	 * Called when an emote event has occurred
	 * @param event Event containing event data
	 */
	void actionPerformed(EmoteEvent event);

}
