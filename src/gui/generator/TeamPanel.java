package gui.generator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import javax.swing.BorderFactory;

import gui.Util;
import gui.components.LffLabel;
import gui.components.LffPanel;
import model.Player;
import model.Team;

public class TeamPanel
	extends LffPanel
{
	private static final long serialVersionUID = 7793336757251311168L;

	private final LffLabel teamNameLabel;

	private final List<PlayerPanel> playerPanels;

	public TeamPanel(Team team, Function<Integer, Boolean> scoringRule, int height)
	{
		teamNameLabel = new LffLabel(
				team.getName() + " (" + team.numberOfScoreablePlayers(scoringRule) + ")",
				Font.BOLD,
				20);

		playerPanels = new LinkedList<>();

		for (Player player : team)
		{
			playerPanels.add(new PlayerPanel(player, scoringRule));
		}

		setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Util.FOREGROUND),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		setPreferredSize(new Dimension(200, height));

		setLayout(new GridLayout(1 + team.numberOfPlayers(), 1));

		add(teamNameLabel);

		for (PlayerPanel playerPanel : playerPanels)
		{
			add(playerPanel);
		}
	}
}
