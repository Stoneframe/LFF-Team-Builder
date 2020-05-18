package teamsbuilder.evolution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.NumberOf.Category;
import model.Team;

public class LffFitnessCalculator
	implements
		FitnessCalculator
{
	private static final double FACTOR = 10;

	private final OptimalTeam optimalTeam;

	private final List<FitnessCalculator> calculators = new LinkedList<>();

	private final Map<List<Team>, Double> cache = new HashMap<>();

	public LffFitnessCalculator(OptimalTeam optimalTeam, Category... categories)
	{
		this.optimalTeam = optimalTeam;

		for (Category category : categories)
		{
			calculators.add(new CategoryFitnessCalculator(category));
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
		private final Category category;

		public CategoryFitnessCalculator(Category category)
		{
			this.category = category;
		}

		@Override
		public double calculate(List<Team> teams)
		{
			return teams.stream().mapToDouble(t -> calculate(t)).average().getAsDouble();
		}

		private double calculate(Team team)
		{
			double diff = Math.abs(optimalTeam.count(category) - team.count(category));

			return FACTOR / (FACTOR + diff);
		}
	}
}
