package teamsbuilder;

import java.util.List;

public class TeamSettings
{
	private final List<String> teamNames;

	private boolean splitNonLockedGroups = false;

	private int numberOfTeams;

	public TeamSettings(List<String> teamNames)
	{
		this.teamNames = teamNames;
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
