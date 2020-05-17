package teamsbuilder;

import java.util.List;
import java.util.function.Function;

public class TeamSettings
{
	private final Function<Integer, Boolean> scoringRule;
	private final List<String> teamNames;

	private boolean splitNonLockedGroups = false;

	private int numberOfTeams;

	public TeamSettings(
		Function<Integer, Boolean> scoringRule,
		List<String> teamNames)
	{
		this.scoringRule = scoringRule;
		this.teamNames = teamNames;
	}

	public Function<Integer, Boolean> getScoringRule()
	{
		return scoringRule;
	}

	public List<String> getTeamNames()
	{
		return teamNames;
	}

	public boolean isSplitNonLockedGroups()
	{
		return splitNonLockedGroups;
	}

	public void setSplitNonLockedGroups(boolean splitNonLockedGroups)
	{
		this.splitNonLockedGroups = splitNonLockedGroups;
	}

	public int getNumberOfTeams()
	{
		return numberOfTeams;
	}

	public void setNumberOfTeams(int numberOfTeams)
	{
		this.numberOfTeams = numberOfTeams;
	}
}
