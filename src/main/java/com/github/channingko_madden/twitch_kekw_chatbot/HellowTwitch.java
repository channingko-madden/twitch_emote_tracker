package com.github.channingko_madden.twitch_kekw_chatbot;

import com.github.channingko_madden.twitch_kekw_chatbot.gui.StartGui;

/*
 * Main class
 * 
 * Wants three arguments passed
 * 	1. Name of twitch channel you want to join (ex. ninja, liihs)
 *  2. Twitch account username to be used
 *  3. OAuth token generated for the twitch account
 */
public class HellowTwitch
{

	public static void main(String[] args)
	{

		System.out.println("Hello Twitch");
		
		if(args.length < 2)
		{
			System.out.println("Must pass Channel Name to follow, your Nickname, and then the Oauth Token");
		}
		else
		{
			StartGui gui = new StartGui();
			//final TwitchSocket testSocket = new TwitchSocket(args[0], args[1], args[2]);
		}
	}

}
