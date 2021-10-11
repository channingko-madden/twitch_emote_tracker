package com.github.channingko_madden.twitch_kekw_chatbot.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
	
	/**
	 * Constructor 
	 * @param theFrame The JFrame for this app.
	 */
	public RunningGui(JFrame theFrame) {

		JPanel background = new JPanel(new BorderLayout());
		// empty border gives a margin between edges of the panel and where the
		// components are placed. Aka it looks nice.
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
	}

}
