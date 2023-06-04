package teamsbuilder.evolution;

import java.util.LinkedList;
import java.util.List;

import model.NumberOf.Category;
import model.Team;
import model.Unit;

public class OptimalTeamFitnessCalculator
	implements
		FitnessCalculator
{
	private final OptimalTeam optimalTeam;

	private final List<FitnessCalculator> calculators = new LinkedList<>();

	public OptimalTeamFitnessCalculator(List<Unit> units, int nbrOfTeams, Category... categories)
	{
		optimalTeam = createOptimalTeam(units, nbrOfTeams);

		for (Category category : categories)
		{
			calculators.add(new OptimalTeamFitnessCalculatorInternal(category));
		}
	}

	@Override
	public double calculate(List<Team> teams)
	{
		return calculators.stream()
			.mapToDouble(c -> c.calculate(teams))
			.sum();
	}

	private OptimalTeam createOptimalTeam(List<Unit> units, int nbrOfTeams)
	{
		return new OptimalTeam(units, nbrOfTeams);
	}

	private class OptimalTeamFitnessCalculatorInternal
		implements
			FitnessCalculator
	{
		private final Category category;

		public OptimalTeamFitnessCalculatorInternal(Category category)
		{
			this.category = category;
		}

		@Override
		public double calculate(List<Team> teams)
		{
			return teams.stream().mapToDouble(t -> calculate(t)).sum();
		}

		private double calculate(Team team)
		{
			return Math.abs(optimalTeam.count(category) - team.count(category));
		}
	}
}
