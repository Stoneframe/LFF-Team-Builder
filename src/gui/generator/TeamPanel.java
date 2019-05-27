package gui.generator;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

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

	private final List<LffLabel> playerNameLabels;

	public TeamPanel(Team team)
	{
		teamNameLabel = new LffLabel(team.getName(), Font.BOLD, 20);

		playerNameLabels = new LinkedList<>();

		for (Player player : team)
		{
			playerNameLabels.add(new LffLabel(player.getName(), Font.PLAIN, 20));
		}

		setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Util.FOREGROUND),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		setLayout(new GridLayout(1 + team.numberOfPlayers(), 1));

		add(teamNameLabel);

		for (LffLabel playerNameLabel : playerNameLabels)
		{
			add(playerNameLabel);
		}
	}
}
