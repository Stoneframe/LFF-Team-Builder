package teamsbuilder.evolution;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static util.ListUtil.getRandom;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import model.NumberOf.Category;
import model.Team;

public class TeamsSetup
{
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

		this.teams = mutate(cloneTeams(teams));
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
		final int maxNbrOfMutations = 10;

		double startingFitness = fitnessCalculator.calculate(teams);

		int i = 0;

		do
		{
			teams = performMutation(teams);
			i++;
		}
		while (fitnessCalculator.calculate(teams) > startingFitness && i < maxNbrOfMutations);

		return teams;
	}

	private List<Team> performMutation(List<Team> teams)
	{
		return new SpecificMutation(getWorstCategory(teams), teams).mutate();
	}

	private Category getWorstCategory(List<Team> teams)
	{
		Function<? super Category, ? extends Integer> largestDiffClassifier = category ->
			{
				IntSummaryStatistics summery = teams.stream()
					.mapToInt(t -> t.count(category))
					.summaryStatistics();

				return summery.getMax() - summery.getMin();
			};

		List<Category> worstCategories = Arrays.stream(categories)
			.collect(groupingBy(largestDiffClassifier, TreeMap::new, toList()))
			.lastEntry()
			.getValue();

		return getRandom(worstCategories);
	}

	private static List<Team> cloneTeams(List<Team> teams)
	{
		return teams.stream()
			.map(t -> new Team(t.getName(), t.getUnits()))
			.collect(Collectors.toList());
	}
}
