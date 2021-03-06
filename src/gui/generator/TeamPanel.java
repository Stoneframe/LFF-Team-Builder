package gui.generator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;

import gui.components.LffPanel;
import gui.util.Util;
import model.Group;
import model.Player;
import model.Team;
import model.Unit;

public class TeamPanel
	extends LffPanel
{
	private static final long serialVersionUID = 7793336757251311168L;

	private final TeamNamePanel teamNameLabel;

	private final List<LffPanel> unitPanels;

	public TeamPanel(Team team)
	{
		teamNameLabel = new TeamNamePanel(team);

		unitPanels = new LinkedList<>();

		for (Unit unit : team)
		{
			if (unit instanceof Player)
			{
				PlayerPanel playerPanel = new PlayerPanel((Player)unit);

				playerPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));

				unitPanels.add(playerPanel);
			}
			else
			{
				unitPanels.add(new GroupPanel((Group)unit));
			}
		}

		setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Util.FOREGROUND),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

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
