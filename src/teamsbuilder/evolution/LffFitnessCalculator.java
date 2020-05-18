package teamsbuilder.evolution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.NumberOf.Counter;
import model.Team;

public class LffFitnessCalculator
	implements
		FitnessCalculator
{
	private static final double FACTOR = 10;

	private final OptimalTeam optimalTeam;

	private final List<FitnessCalculator> calculators = new LinkedList<>();

	private final Map<List<Team>, Double> cache = new HashMap<>();

	public LffFitnessCalculator(OptimalTeam optimalTeam, Counter... counters)
	{
		this.optimalTeam = optimalTeam;

		for (Counter counter : counters)
		{
			calculators.add(new CategoryFitnessCalculator(counter));
		}
	}

	@Override
	public double calculate(List<Team> teams)
	{
		return cache.computeIfAbsent(teams, t -> calculateFitness(t));
	}

	private double calculateFitness(List<Team> teams)
	{
		return calculators.stream()
			.mapToDouble(c -> c.calculate(teams))
			.average()
			.getAsDouble();
	}

	private class CategoryFitnessCalculator
		implements
			FitnessCalculator
	{
		private final Counter counter;

		public CategoryFitnessCalculator(Counter counter)
		{
			this.counter = counter;
		}

		@Override
		public double calculate(List<Team> teams)
		{
			return teams.stream().mapToDouble(t -> calculate(t)).average().getAsDouble();
		}

		private double calculate(Team team)
		{
			double diff = Math.abs(optimalTeam.count(counter) - team.count(counter));

			return FACTOR / (FACTOR + diff);
		}
	}
}
