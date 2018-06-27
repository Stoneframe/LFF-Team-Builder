package teamsbuilder;

import java.util.List;
import java.util.function.Function;

import model.Team;

public class TeamsValidator
{
	private Function<Integer, Boolean> scoringRule;

	private int minimumNumberOfPlayers;
	private int maximumNumberOfPlayers;

	public TeamsValidator(
			Function<Integer, Boolean> scoringRule,
			int minimumNumberOfPlayers,
			int maximumNumberOfPlayers)
	{
		this.scoringRule = scoringRule;

		this.minimumNumberOfPlayers = minimumNumberOfPlayers;
		this.maximumNumberOfPlayers = maximumNumberOfPlayers;
	}

	public boolean areValid(List<Team> teams)
	{
		return allTeamsHaveScoreablePlayers(teams)
				&& noTeamsHaveTooFewPlayers(teams)
				&& noTeamsHaveTooManyPlayers(teams);
	}

	private boolean allTeamsHaveScoreablePlayers(List<Team> teams)
	{
		return teams.stream().allMatch(t -> t.numberOfScoreablePlayers(scoringRule) > 0);
	}

	private boolean noTeamsHaveTooFewPlayers(List<Team> teams)
	{
		return teams.stream().allMatch(t -> t.numberOfPlayers() >= minimumNumberOfPlayers);
	}

	private boolean noTeamsHaveTooManyPlayers(List<Team> teams)
	{
		return teams.stream().allMatch(t -> t.numberOfPlayers() <= maximumNumberOfPlayers);
	}
}
