package teamsbuilder.evolution;

import java.util.List;

import model.NumberOf;
import model.Team;

public class ScoreAbleFitnessCalculator
	implements
		FitnessCalculator
{
	private static final double FACTOR = 10;

	private final double optimalValue;

	public ScoreAbleFitnessCalculator(double optimalValue)
	{
		this.optimalValue = optimalValue;
	}

	@Override
	public double calculate(List<Team> teams)
	{
		return teams.stream().mapToDouble(t -> calculate(t)).average().getAsDouble();
	}

	private double calculate(Team team)
	{
		double diff = Math.abs(optimalValue - team.count(NumberOf.SCORE_ABLE));
		
		return FACTOR / (FACTOR + diff);
	}
}
