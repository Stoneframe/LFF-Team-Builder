package gui;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;

import gui.components.LffIcon;
import gui.components.LffLabel;
import gui.components.LffPanel;

public class LundsFFPanel
	extends LffPanel
{
	private static final long serialVersionUID = 901334533317340280L;

	public LundsFFPanel()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT, 50, 20));

		
		
		add(new JLabel(new LffIcon()));
		add(new LffLabel("Lunds FF", Font.BOLD, 84));
	}
}
