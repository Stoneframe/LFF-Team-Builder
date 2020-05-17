package gui.generator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import gui.components.LffLabel;
import gui.components.LffPanel;
import gui.util.Util;
import model.Team;

public class TeamListPanel
	extends LffPanel
{
	private static final long serialVersionUID = -6088180037000149977L;

	private final LffLabel titleLabel;

	private final LffPanel teamsPanel;

	private final JScrollPane scrollPane;

	public TeamListPanel()
	{
		titleLabel = new LffLabel("Lag", Font.BOLD, 40);

		teamsPanel = new LffPanel();

		LffPanel teamsWrapperPanel = new LffPanel(new GridBagLayout());

		teamsWrapperPanel.setLayout(new GridBagLayout());
		teamsWrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		teamsWrapperPanel.add(
			teamsPanel,
			new GridBagConstraints(
				0,
				0,
				0,
				0,
				1,
				1,
				GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0,
				0));

		scrollPane = new JScrollPane(teamsWrapperPanel);
		scrollPane.setBorder(BorderFactory.createLineBorder(Util.FOREGROUND, 2));

		setPreferredSize(new Dimension(500, 0));

		setLayout(new BorderLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.anchor = GridBagConstraints.NORTHWEST;

		gbc.gridy = 0;
		gbc.gridx = 0;
		add(titleLabel, BorderLayout.NORTH);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.weighty = 1;
		add(scrollPane, BorderLayout.CENTER);
	}

	public void showTeams(List<Team> teams, Function<Integer, Boolean> scoringRule)
	{
		teamsPanel.removeAll();

		int nbrOfPlayersInLargestTeam = teams.stream()
			.mapToInt(t -> t.numberOfPlayers())
			.max()
			.getAsInt();

		int rows;

		if (nbrOfPlayersInLargestTeam <= 3)
		{
			rows = 3;
		}
		else if (nbrOfPlayersInLargestTeam <= 7)
		{
			rows = 2;
		}
		else
		{
			rows = 1;
		}

		int cols = teams.size() / rows + 1;

		teamsPanel.setLayout(new GridLayout(rows, cols, 10, 10));

		for (Team team : teams)
		{
			teamsPanel.add(new TeamPanel(team, scoringRule));
		}

		teamsPanel.revalidate();
	}
}
