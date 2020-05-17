package gui.generator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import gui.components.LffIcon;
import gui.components.LffLabel;
import gui.components.LffPanel;
import model.Player;

public class PlayerPanel
	extends LffPanel
{
	private static final long serialVersionUID = 8734376449532856067L;

	private final LffIcon greenFootballIcon;

	private final LffLabel iconLabel;
	private final LffLabel nameLabel;
	private final LffLabel ageLabel;

	public PlayerPanel(Player player)
	{
		greenFootballIcon = LffIcon.getGreenFootball();

		iconLabel = new LffLabel();
		iconLabel.setPreferredSize(new Dimension(15, 15));

		if (player.isScoreAble())
		{
			iconLabel.setIcon(greenFootballIcon);
		}

		nameLabel = new LffLabel(player.getName(), Font.PLAIN, 20);
		ageLabel = new LffLabel(Integer.toString(player.getAge()), Font.PLAIN, 20);

		setLayout(new BorderLayout(5, 0));

		add(iconLabel, BorderLayout.WEST);
		add(nameLabel, BorderLayout.CENTER);
		add(ageLabel, BorderLayout.EAST);
	}
}
