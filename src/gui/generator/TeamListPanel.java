package gui.generator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.util.List;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import gui.Util;
import gui.components.LffLabel;
import gui.components.LffPanel;
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

		teamsPanel = new LffPanel(new FlowLayout(FlowLayout.LEFT));

		scrollPane = new JScrollPane(teamsPanel);
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
		gbc.weighty = 100;
		add(scrollPane, BorderLayout.CENTER);
	}

	public void showTeams(List<Team> teams, Function<Integer, Boolean> scoringRule)
	{
		int height = 35 + 30 * teams.stream().mapToInt(t -> t.numberOfPlayers()).max().getAsInt();

		for (Team team : teams)
		{
			TeamPanel teamPanel = new TeamPanel(team, scoringRule, height);

			teamsPanel.add(teamPanel);
		}

		teamsPanel.revalidate();
	}
}
