package teamsbuilder.evolution;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import model.NumberOf.Category;
import model.Team;

public class TeamsSetup
{
	private final Random random = new Random();

	private final FitnessCalculator fitnessCalculator;
	private final Category[] categories;

	private final List<Team> teams;

	private Double cachedFitness;

	public TeamsSetup(
		FitnessCalculator fitnessCalculator,
		Category[] categories,
		List<Team> teams)
	{
		this.fitnessCalculator = fitnessCalculator;
		this.categories = categories;

		this.teams = mutate(teams);
	}

	public List<Team> getTeams()
	{
		return teams;
	}

	public double getFitness()
	{
		if (cachedFitness == null)
		{
			cachedFitness = fitnessCalculator.calculate(teams);
		}

		return cachedFitness;
	}

	public List<TeamsSetup> reproduce(int nbrOfChildren)
	{
		List<TeamsSetup> setups = new LinkedList<>();

		for (int i = 0; i < nbrOfChildren; i++)
		{
			setups.add(new TeamsSetup(fitnessCalculator, categories, teams));
		}

		return setups;
	}

	@Override
	public String toString()
	{
		return "Fitness: " + getFitness();
	}

	private List<Team> mutate(List<Team> teams)
	{
		double startingFitness = fitnessCalculator.calculate(teams);

		int i = 0;

		do
		{
			teams = performMutation(teams);
			i++;
		}
		while (fitnessCalculator.calculate(teams) > startingFitness && i < 10);

		return teams;
	}

	private List<Team> performMutation(List<Team> teams)
	{
		// switch (random.nextInt(2))
		switch (0)
		{
			case 0:
				return new SpecificMutation(getWorstCategory(teams), teams).mutate();
			case 1:
				return new RandomMutation(teams).mutate();
			default:
				throw new RuntimeException();
		}
	}

	private Category getWorstCategory(List<Team> teams)
	{
		// Comparator<? super Category> comparator = (c1, c2) ->
		// {
		// IntSummaryStatistics summery1 = teams.stream()
		// .mapToInt(t -> t.count(c1))
		// .summaryStatistics();
		//
		// IntSummaryStatistics summery2 = teams.stream()
		// .mapToInt(t -> t.count(c2))
		// .summaryStatistics();
		//
		// return Integer.compare(
		// summery1.getMax() - summery1.getMin(),
		// summery2.getMax() - summery2.getMin());
		// };
		//
		// return Arrays.stream(categories).max(comparator).get();

		Map<Integer, List<Category>> map = Arrays.stream(categories)
			.collect(Collectors.groupingBy(c ->
				{
					IntSummaryStatistics summery = teams.stream()
						.mapToInt(t -> t.count(c))
						.summaryStatistics();

					return summery.getMax() - summery.getMin();
				}));

		int highestDiff = map.keySet().stream().max(Integer::compareTo).get();

		List<Category> categoriesWithHighestDiff = map.get(highestDiff);

		int randomIndex = random.nextInt(categoriesWithHighestDiff.size());

		return categoriesWithHighestDiff.get(randomIndex);
	}
}
