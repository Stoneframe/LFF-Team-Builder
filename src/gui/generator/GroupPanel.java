package gui.generator;

import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import gui.components.LffPanel;
import gui.util.Util;
import model.Group;
import model.NumberOf;
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

		Border border = group.isLocked()
				? BorderFactory.createLineBorder(Util.FOREGROUND)
				: BorderFactory.createDashedBorder(Util.FOREGROUND);

		setBorder(border);

		setLayout(new GridLayout(group.count(NumberOf.PLAYERS), 1, 0, 4));

		for (PlayerPanel playerPanel : playerPanels)
		{
			add(playerPanel);
		}
	}
}
