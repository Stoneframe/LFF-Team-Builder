package components;

import java.awt.Font;

import javax.swing.JCheckBox;

import registrator.Util;

public class LffCheckBox
	extends JCheckBox
{
	private static final long serialVersionUID = 9181193000368287137L;

	public LffCheckBox(String text)
	{
		super(text);

		setFont(new Font("Dialog", Font.BOLD, 15));

		setForeground(Util.FOREGROUND);
		setBackground(Util.BACKGROUND);
	}
}
