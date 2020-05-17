package teamsbuilder.evolution;

import java.util.List;

import model.Team;

public interface FitnessCalculator
{
	double calculate(List<Team> teams);
}