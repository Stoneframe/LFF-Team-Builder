package gui.components;

import java.awt.Font;

import javax.swing.JButton;

import gui.util.Util;

public class LffButton
	extends JButton
{
	private static final long serialVersionUID = -6702430449574296387L;

	public LffButton(String text)
	{
		super(text);

		setForeground(Util.BACKGROUND);
		setBackground(Util.FOREGROUND);
		setOpaque(true);
		setBorderPainted(false);
	}

	public LffButton(String text, boolean isEnabled)
	{
		this(text);

		setEnabled(isEnabled);
	}

	public LffButton(String text, int size)
	{
		this(text);

		setFont(new Font("Dialog", Font.PLAIN, size));
	}

	public LffButton(String text, int size, boolean isEnabled)
	{
		this(text);

		setEnabled(isEnabled);
		setFont(new Font("Dialog", Font.PLAIN, size));
	}
}
