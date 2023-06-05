package teamsbuilder.evolution;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import model.Group;
import model.Team;
import model.Unit;
import model.Group.GroupSplit;

public class RandomMutation
	implements
		Mutation
{
	private static final Random random = new Random();

	private final List<Team> teams;

	private Team team1;
	private Team team2;

	private Unit unit1;
	private Unit unit2;

	public RandomMutation(List<Team> teams)
	{
		this.teams = cloneTeams(teams);
	}

	@Override
	public List<Team> mutate()
	{
		selectRandomTeams();
		selectUnitFromTeam1();
		selectUnitFromTeam2();
		moveOrSwapUnits();

		return teams;
	}

	private static List<Team> cloneTeams(List<Team> teams)
	{
		return teams.stream()
			.map(t -> new Team(t.getName(), t.getUnits()))
			.collect(Collectors.toList());
	}

	private void selectRandomTeams()
	{
		team1 = teams.get(random.nextInt(teams.size()));
		team2 = teams.get(random.nextInt(teams.size()));
	}

	private void selectUnitFromTeam1()
	{
		unit1 = getRandomUnit(team1.getUnits());

		if (isUnitSplitAble(unit1) && random.nextBoolean())
		{
			unit1 = getSplitUnit(team1, (Group)unit1);
		}
	}

	private void selectUnitFromTeam2()
	{
		unit2 = getRandomUnit(team2.getUnits());

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

	private boolean isUnitSplitAble(Unit unit)
	{
		return unit instanceof Group && !((Group)unit).isLocked();
	}

	private static Unit getSplitUnit(Team team, Group group)
	{
		GroupSplit split = split(team, group);

		return random.nextBoolean()
			? split.getUnit1()
			: split.getUnit2();
	}

	private static GroupSplit split(Team team, Group group)
	{
		GroupSplit split = group.split();

		team.remove(group);
		team.add(split.getUnit1());
		team.add(split.getUnit2());

		return split;
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
}
