package teamBuilder;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Algorithm
{
	private int numberOfTeams;

	private Function<Integer, Boolean> scoringRule;

	public Algorithm(int numberOfTeams, Function<Integer, Boolean> scoringRule)
	{
		this.numberOfTeams = numberOfTeams;
		this.scoringRule = scoringRule;
	}

	public List<Team> createTeams(List<Unit> units)
	{
		List<Unit> scoreableUnits = new LinkedList<>();
		List<Unit> remainingUnits = new LinkedList<>();

		splitUnitsByPlayersThatCanScore(units, scoreableUnits, remainingUnits);

		scoreableUnits = sortUnitsByNumberOfPlayersThatCanScore(scoreableUnits);
		remainingUnits = sortUnitsByNumberOfPlayers(remainingUnits);

		List<Team> teams =
				Stream.generate(Team::new).limit(numberOfTeams).collect(Collectors.toList());

		for (Unit unit : scoreableUnits)
		{
			addUnitToTeam(unit, getTeamWithLeastPlayersThatCanScore(teams));
		}

		for (Unit unit : remainingUnits)
		{
			addUnitToTeam(unit, getTeamWithLeastPlayers(teams));
		}

		return teams;
	}

	private void splitUnitsByPlayersThatCanScore(
			List<Unit> allUnits,
			List<Unit> scoreableUnits,
			List<Unit> remainingUnits)
	{
		for (Unit unit : allUnits)
		{
			if (unit.numberOfPlayersThatCanScore(scoringRule) > 0)
			{
				scoreableUnits.add(unit);
			}
			else
			{
				remainingUnits.add(unit);
			}
		}
	}

	private List<Unit> sortUnitsByNumberOfPlayersThatCanScore(List<Unit> units)
	{
		return units
			.stream()
			.sorted(
				Comparator
					.comparing((Unit u) -> u.numberOfPlayersThatCanScore(scoringRule))
					.reversed())
			.collect(Collectors.toList());
	}

	private List<Unit> sortUnitsByNumberOfPlayers(List<Unit> units)
	{
		return units
			.stream()
			.sorted(Comparator.comparing((Unit u) -> u.numberOfPlayers()).reversed())
			.collect(Collectors.toList());
	}

	private Team getTeamWithLeastPlayersThatCanScore(List<Team> teams)
	{
		List<Team> sortedTeams = teams
			.stream()
			.sorted(Comparator.comparing((Team t) -> t.numberOfPlayersThatCanScore(scoringRule)))
			.collect(Collectors.toList());

		return sortedTeams
			.stream()
			.findFirst()
			.orElse(sortedTeams.get(0));
	}

	private Team getTeamWithLeastPlayers(List<Team> teams)
	{
		List<Team> sortedTeams = teams
			.stream()
			.sorted(Comparator.comparing((Team t) -> t.numberOfPlayers()))
			.collect(Collectors.toList());

		return sortedTeams
			.stream()
			.findFirst()
			.orElse(sortedTeams.get(0));
	}

	private static void addUnitToTeam(Unit unit, Team team)
	{
		if (unit instanceof Player)
		{
			team.add((Player)unit);
		}
		else
		{
			for (Unit subUnit : (Group)unit)
			{
				addUnitToTeam(subUnit, team);
			}
		}
	}
}
