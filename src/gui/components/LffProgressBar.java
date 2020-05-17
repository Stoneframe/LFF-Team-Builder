package gui.components;

import javax.swing.JProgressBar;

import gui.util.Util;

public class LffProgressBar
	extends JProgressBar
{
	private static final long serialVersionUID = -1693983919694247289L;

	public LffProgressBar()
	{
		setStringPainted(true);
		setForeground(Util.FOREGROUND);
	}
}
