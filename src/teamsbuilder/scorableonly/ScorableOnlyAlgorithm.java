package teamsbuilder.scorableonly;

import java.util.List;

import model.Team;
import model.Unit;
import teamsbuilder.Algorithm;
import teamsbuilder.TeamSettings;

public class ScorableOnlyAlgorithm
	implements
		Algorithm
{
	@Override
	public List<Team> createTeams(List<Unit> units, TeamSettings settings)
	{
		Layer algorithm = new UnitDividerLayer(settings.getScoringRule(), settings.getTeamNames());

		if (settings.isSplitNonLockedGroups())
		{
			algorithm = new GroupSplitterLayer(
				algorithm,
				settings.getScoringRule(),
				new TeamsValidator(
					settings.getScoringRule(),
					settings.getMinimumNumberOfPlayers(),
					settings.getMaximumNumberOfPlayers()));
		}

		return algorithm.createTeams(units, settings.getNumberOfTeams());
	}
}
