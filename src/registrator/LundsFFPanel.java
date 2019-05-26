package registrator;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;

import registrator.components.LffIcon;
import registrator.components.LffLabel;
import registrator.components.LffPanel;

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
