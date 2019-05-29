package teamsbuilder;

import java.util.List;
import java.util.function.Function;

import model.Team;
import model.Unit;

public class TeamsBuilder
{
	private boolean splitNonLockedGroups = false;

	private Function<Integer, Boolean> scoringRule;

	private int minimumNumberOfPlayers;
	private int maximumNumberOfPlayers;

	private List<String> teamNames;

	public TeamsBuilder(
			Function<Integer, Boolean> scoringRule,
			List<String> teamNames)
	{
		this.scoringRule = scoringRule;
		this.teamNames = teamNames;
	}

	public boolean isSplitNonLockedGroups()
	{
		return splitNonLockedGroups;
	}

	public void setSplitNonLockedGroups(boolean splitNonLockedGroups)
	{
		this.splitNonLockedGroups = splitNonLockedGroups;
	}

	public int getMinimumNumberOfPlayers()
	{
		return minimumNumberOfPlayers;
	}

	public void setMinimumNumberOfPlayers(int minimumNumberOfPlayers)
	{
		this.minimumNumberOfPlayers = minimumNumberOfPlayers;
	}

	public int getMaximumNumberOfPlayers()
	{
		return maximumNumberOfPlayers;
	}

	public void setMaximumNumberOfPlayers(int maximumNumberOfPlayers)
	{
		this.maximumNumberOfPlayers = maximumNumberOfPlayers;
	}

	public List<Team> createTeams(List<Unit> units, int numberOfTeams)
	{
		Layer algorithm = new Algorithm(scoringRule, teamNames);

		if (splitNonLockedGroups)
		{
			algorithm = new GroupSplitterLayer(
					algorithm,
					scoringRule,
					new TeamsValidator(
							scoringRule,
							minimumNumberOfPlayers,
							maximumNumberOfPlayers));
		}

		return algorithm.createTeams(units, numberOfTeams);
	}
}
