package gui.components;

import java.awt.Font;

import javax.swing.JLabel;

import gui.Util;

public class LffLabel
	extends JLabel
{
	private static final long serialVersionUID = 6543797572089446210L;

	public LffLabel(String text)
	{
		this(text, Font.BOLD, 20);
	}

	public LffLabel(String text, int style, int size)
	{
		super(text);

		setForeground(Util.FOREGROUND);
		setBackground(Util.BACKGROUND);

		setFont(new Font("Dialog", style, size));
	}
}
