package gui.components;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JScrollPane;

import gui.Util;

public class LffScrollPane
	extends JScrollPane
{
	private static final long serialVersionUID = -3450087759986457841L;

	public LffScrollPane(JList<?> list, Dimension dimension)
	{
		super(list);

		setBorder(BorderFactory.createLineBorder(Util.FOREGROUND, 2));
		setPreferredSize(dimension);
		setBackground(Util.BACKGROUND);
	}
}
