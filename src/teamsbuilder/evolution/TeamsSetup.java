package teamsbuilder.evolution;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import model.Group;
import model.Group.GroupSplit;
import model.NumberOf.Category;
import model.Team;
import model.Unit;

public class TeamsSetup
{
	private final Random random = new Random();

	private final List<Team> teams;
	private final FitnessCalculator fitnessCalculator;

	private final Category[] categories;

	private Double cachedFitness;

	private Team team1;
	private Team team2;

	private Unit unit1;
	private Unit unit2;

	public TeamsSetup(List<Team> teams, FitnessCalculator fitnessCalculator, Category[] categories)
	{
		this.teams = cloneTeams(teams);
		this.fitnessCalculator = fitnessCalculator;

		this.categories = categories;

		mutate();
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

	public List<TeamsSetup> reproduce()
	{
		List<TeamsSetup> setups = new LinkedList<>();

		for (int i = 0; i < 100; i++)
		{
			setups.add(new TeamsSetup(teams, fitnessCalculator, categories));
		}

		return setups;
	}

	@Override
	public String toString()
	{
		return "Fitness: " + getFitness();
	}

	private List<Team> cloneTeams(List<Team> teams)
	{
		return teams.stream()
			.map(t -> new Team(t.getName(), t.getUnits()))
			.collect(Collectors.toList());
	}

	private void mutate()
	{
		do
		{
			int index = random.nextInt(categories.length);

			mutate(categories[index]);
		}
		while (random.nextBoolean());
	}

	private void mutate(Category category)
	{
		selectTeams(category);
		selectUnitFromTeam1(category);
		selectUnitFromTeam2();
		moveOrSwapUnits();
	}

	protected void selectTeams(Category category)
	{
		team1 = teams.stream().sorted(by(category).reversed()).findFirst().get();
		team2 = teams.stream().sorted(by(category)).findFirst().get();
	}

	private void selectUnitFromTeam1(Category category)
	{
		unit1 = getUnitFromTeam1(category);

		if (isUnitSplitAble(unit1) && random.nextBoolean())
		{
			unit1 = getSplitUnit((Group)unit1);
		}
	}

	private void selectUnitFromTeam2()
	{
		unit2 = getRandomUnit(team2.getUnits());
	}

	private Unit getUnitFromTeam1(Category category)
	{
		List<Unit> units = team1.getUnits()
			.stream()
			.filter(u -> u.count(category) > 0)
			.collect(Collectors.toList());

		return getRandomUnit(units);
	}

	private Unit getRandomUnit(List<Unit> units)
	{
		if (units.size() == 0)
		{
			return null;
		}

		int index = random.nextInt(units.size());

		return units.get(index);
	}

	private void moveOrSwapUnits()
	{
		moveUnit1();
		moveUnit2();
	}

	private void moveUnit1()
	{
		if (unit1 != null && random.nextBoolean())
		{
			team1.remove(unit1);
			team2.add(unit1);
		}
	}

	private void moveUnit2()
	{
		if (unit2 != null && random.nextBoolean())
		{
			team2.remove(unit2);
			team1.add(unit2);
		}
	}

	private Unit getSplitUnit(Group group)
	{
		GroupSplit split = split(group);

		return random.nextBoolean()
				? split.getUnit1()
				: split.getUnit2();
	}

	private boolean isUnitSplitAble(Unit unit)
	{
		return unit instanceof Group && !((Group)unit1).isLocked();
	}

	private GroupSplit split(Group group)
	{
		GroupSplit split = group.split();

		team1.remove(group);
		team1.add(split.getUnit1());
		team1.add(split.getUnit2());

		return split;
	}

	private Comparator<? super Unit> by(Category category)
	{
		return (unit1, unit2) -> Integer.compare(unit1.count(category), unit2.count(category));
	}

	// private class Mutation
	// {
	// private final Category category;
	//
	// private Team team1;
	// private Team team2;
	//
	// private Unit unit1;
	// private Unit unit2;
	//
	// public Mutation(Category category)
	// {
	// this.category = category;
	// }
	//
	// public void perform()
	// {
	// selectTeams();
	// selectUnitFromTeam1();
	// selectUnitFromTeam2();
	// moveOrSwapUnits();
	// }
	//
	// protected void selectTeams()
	// {
	// team1 = teams.stream().sorted(by(category).reversed()).findFirst().get();
	// team2 = teams.stream().sorted(by(category)).findFirst().get();
	// }
	//
	// private void selectUnitFromTeam1()
	// {
	// unit1 = getUnitFromTeam1();
	//
	// if (isUnitSplitAble(unit1) && random.nextBoolean())
	// {
	// unit1 = getSplitUnit((Group)unit1);
	// }
	// }
	//
	// private void selectUnitFromTeam2()
	// {
	// unit2 = getRandomUnit(team2.getUnits());
	// }
	//
	// private Unit getUnitFromTeam1()
	// {
	// List<Unit> units = team1.getUnits()
	// .stream()
	// .filter(u -> u.count(category) > 0)
	// .collect(Collectors.toList());
	//
	// return getRandomUnit(units);
	// }
	//
	// private Unit getRandomUnit(List<Unit> units)
	// {
	// if (units.size() == 0)
	// {
	// return null;
	// }
	//
	// int index = random.nextInt(units.size());
	//
	// return units.get(index);
	// }
	//
	// private void moveOrSwapUnits()
	// {
	// moveUnit1();
	// moveUnit2();
	// }
	//
	// private void moveUnit1()
	// {
	// if (unit1 != null && random.nextBoolean())
	// {
	// team1.remove(unit1);
	// team2.add(unit1);
	// }
	// }
	//
	// private void moveUnit2()
	// {
	// if (unit2 != null && random.nextBoolean())
	// {
	// team2.remove(unit2);
	// team1.add(unit2);
	// }
	// }
	//
	// private Unit getSplitUnit(Group group)
	// {
	// GroupSplit split = split(group);
	//
	// return random.nextBoolean()
	// ? split.getUnit1()
	// : split.getUnit2();
	// }
	//
	// private boolean isUnitSplitAble(Unit unit)
	// {
	// return unit instanceof Group && !((Group)unit1).isLocked();
	// }
	//
	// private GroupSplit split(Group group)
	// {
	// GroupSplit split = group.split();
	//
	// team1.remove(group);
	// team1.add(split.getUnit1());
	// team1.add(split.getUnit2());
	//
	// return split;
	// }
	//
	// private Comparator<? super Unit> by(Category category)
	// {
	// return (unit1, unit2) -> Integer.compare(unit1.count(category),
	// unit2.count(category));
	// }
	// }
}
