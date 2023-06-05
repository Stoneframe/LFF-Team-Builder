package teamsbuilder.evolution;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static util.ListUtil.getRandom;

import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import model.Group;
import model.Group.GroupSplit;
import model.NumberOf.Category;
import model.Team;
import model.Unit;

public class SpecificMutation
	implements
		Mutation
{
	private static final Random random = new Random();

	private final Category category;
	private final List<Team> teams;

	private Team team1;
	private Team team2;

	private Unit unit1;
	private Unit unit2;

	public SpecificMutation(Category category, List<Team> teams)
	{
		this.category = category;

		this.teams = teams;
	}

	@Override
	public List<Team> mutate()
	{
		selectTeams(category);
		selectUnitFromTeam1(category);
		selectUnitFromTeam2();
		moveOrSwapUnits();

		return teams;
	}

	private void selectTeams(Category category)
	{
		team1 = getRandom(getTeamsWithHighestOf(category));
		team2 = getRandom(getTeamsWithLowestOf(category));
	}

	private void selectUnitFromTeam1(Category category)
	{
		unit1 = getUnitFromTeam1(category);

		if (isUnitSplitAble(unit1) && random.nextBoolean())
		{
			unit1 = getSplitUnit(team1, (Group)unit1);
		}
	}

	private void selectUnitFromTeam2()
	{
		unit2 = getRandom(team2.getUnits());

		if (isUnitSplitAble(unit2) && random.nextBoolean())
		{
			unit2 = getSplitUnit(team2, (Group)unit2);
		}
	}

	private void moveOrSwapUnits()
	{
		moveUnit1();
		moveUnit2();
	}

	private Unit getUnitFromTeam1(Category category)
	{
		List<Unit> units = team1.getUnits()
			.stream()
			.filter(u -> u.count(category) > 0)
			.collect(Collectors.toList());

		return getRandomUnit(category, units);
	}

	private boolean isUnitSplitAble(Unit unit)
	{
		return unit instanceof Group && !((Group)unit).isLocked();
	}

	private Unit getSplitUnit(Team team, Group group)
	{
		GroupSplit split = split(team, group);

		return random.nextBoolean()
			? split.getUnit1()
			: split.getUnit2();
	}

	private GroupSplit split(Team team, Group group)
	{
		GroupSplit split = group.split();

		team.remove(group);
		team.add(split.getUnit1());
		team.add(split.getUnit2());

		return split;
	}

	private Unit getRandomUnit(Category category, List<Unit> units)
	{
		if (units.size() == 0)
		{
			return null;
		}

		List<Unit> unitsOfCategory = units.stream()
			.filter(u -> u.count(category) > 0)
			.collect(Collectors.toList());

		return getRandom(unitsOfCategory);
	}

	private void moveUnit1()
	{
		if (unit1 != null)
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

	private List<Team> getTeamsWithHighestOf(Category category)
	{
		return teams.stream()
			.collect(groupingBy(t -> t.count(category), TreeMap::new, toList()))
			.lastEntry()
			.getValue();
	}

	private List<Team> getTeamsWithLowestOf(Category category)
	{
		return teams.stream()
			.collect(groupingBy(t -> t.count(category), TreeMap::new, toList()))
			.firstEntry()
			.getValue();
	}
}
