package components;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import registrator.Util;

public class LffPanel
	extends JPanel
{
	private static final long serialVersionUID = -8555718122379232876L;

	public LffPanel()
	{
		setBackground(Util.BACKGROUND);
	}

	public LffPanel(LayoutManager layoutManager)
	{
		super(layoutManager);
		setBackground(Util.BACKGROUND);
	}
}
