package teamsbuilder;

import java.util.List;

import model.Team;
import model.Unit;

public interface Layer
{
	List<Team> createTeams(List<Unit> units, int numberOfTeams);
}