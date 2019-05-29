package gui.generator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import javax.swing.BorderFactory;

import gui.Util;
import gui.components.LffLabel;
import gui.components.LffPanel;
import model.Group;
import model.Player;
import model.Team;
import model.Unit;

public class TeamPanel
	extends LffPanel
{
	private static final long serialVersionUID = 7793336757251311168L;

	private final LffLabel teamNameLabel;

	private final List<LffPanel> unitPanels;

	public TeamPanel(Team team, Function<Integer, Boolean> scoringRule, int height)
	{
		teamNameLabel = new LffLabel(
				team.getName() + " (" + team.numberOfScoreablePlayers(scoringRule) + ")",
				Font.BOLD,
				20);

		unitPanels = new LinkedList<>();

		for (Unit unit : team)
		{
			if (unit instanceof Player)
			{
				PlayerPanel playerPanel = new PlayerPanel((Player)unit, scoringRule);

				playerPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));

				unitPanels.add(playerPanel);
			}
			else
			{
				unitPanels.add(new GroupPanel((Group)unit, scoringRule));
			}
		}

		setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Util.FOREGROUND),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		setPreferredSize(new Dimension(300, height));

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(2, 0, 2, 0);
		gbc.weightx = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;

		add(teamNameLabel, gbc);

		for (LffPanel unitPanel : unitPanels)
		{
			gbc.gridy++;
			add(unitPanel, gbc);
		}

		gbc.gridy++;
		gbc.weighty = 1;
		add(new LffPanel(), gbc);
	}
}
