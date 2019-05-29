package gui.generator;

import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import gui.Util;
import gui.components.LffPanel;
import model.Group;
import model.Player;

public class GroupPanel
	extends LffPanel
{
	private static final long serialVersionUID = 5355551220864101314L;

	private final List<PlayerPanel> playerPanels;

	public GroupPanel(Group group, Function<Integer, Boolean> scoringRule)
	{
		playerPanels = new LinkedList<>();

		for (Player player : group)
		{
			PlayerPanel playerPanel = new PlayerPanel(player, scoringRule);

			playerPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

			playerPanels.add(playerPanel);
		}

		Border outerBorder = group.isLocked()
				? BorderFactory.createLineBorder(Util.FOREGROUND)
				: BorderFactory.createDashedBorder(Util.FOREGROUND);

		setBorder(outerBorder);

		setLayout(new GridLayout(group.numberOfPlayers(), 1, 0, 3));

		for (PlayerPanel playerPanel : playerPanels)
		{
			add(playerPanel);
		}
	}
}