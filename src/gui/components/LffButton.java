package gui.components;

import java.awt.Font;

import javax.swing.JButton;

import gui.Util;

public class LffButton
	extends JButton
{
	private static final long serialVersionUID = -6702430449574296387L;

	public LffButton(String text)
	{
		super(text);

		setForeground(Util.BACKGROUND);
		setBackground(Util.FOREGROUND);
	}

	public LffButton(String text, boolean enabled)
	{
		this(text);

		setEnabled(enabled);
	}

	public LffButton(String text, int size)
	{
		this(text);

		setFont(new Font("Dialog", Font.PLAIN, size));
	}
}
