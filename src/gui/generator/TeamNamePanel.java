package gui.generator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.function.Function;

import javax.swing.JSeparator;

import gui.components.LffIcon;
import gui.components.LffLabel;
import gui.components.LffPanel;
import gui.util.Util;
import model.NumberOf;
import model.Team;

public class TeamNamePanel
	extends LffPanel
{
	private static final long serialVersionUID = 2378361021534711449L;

	private final LffLabel teamNameLabel;
	private final LffLabel nbrScoreablePlayersLabel;
	private final LffLabel iconLabel;

	public TeamNamePanel(Team team, Function<Integer, Boolean> scoringRule)
	{
		teamNameLabel = new LffLabel(team.getName());
		nbrScoreablePlayersLabel = new LffLabel(
			Integer.toString(team.count(NumberOf.SCORE_ABLE)));
		iconLabel = new LffLabel();
		iconLabel.setIcon(LffIcon.getGreenFootball());

		LffPanel east = new LffPanel(new GridLayout(1, 2));

		east.add(nbrScoreablePlayersLabel);
		east.add(iconLabel);

		setLayout(new BorderLayout());

		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		separator.setForeground(Util.FOREGROUND);

		add(teamNameLabel, BorderLayout.CENTER);
		add(east, BorderLayout.EAST);
		add(separator, BorderLayout.SOUTH);
	}
}
