package teamsbuilder;

import java.util.List;

import model.Team;
import model.Unit;

public interface Algorithm
{
	List<Team> createTeams(List<Unit> units, TeamSettings settings);
}