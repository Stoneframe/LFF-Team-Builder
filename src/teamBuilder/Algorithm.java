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

		splitUnitsByScoreablePlayers(units, scoreableUnits, remainingUnits);

		scoreableUnits = sortUnitsByNumberOfScoreablePlayersDescending(scoreableUnits);
		remainingUnits = sortUnitsByNumberOfPlayersDescending(remainingUnits);

		List<Team> teams =
				Stream.generate(Team::new).limit(numberOfTeams).collect(Collectors.toList());

		for (Unit unit : scoreableUnits)
		{
			Team team = getTeamWithLeastScoreablePlayers(teams);

			team.add(unit);
		}

		for (Unit unit : remainingUnits)
		{
			Team team = getTeamWithLeastPlayers(teams);

			team.add(unit);
		}

		return teams;
	}

	private void splitUnitsByScoreablePlayers(
			List<Unit> allUnits,
			List<Unit> scoreableUnits,
			List<Unit> remainingUnits)
	{
		for (Unit unit : allUnits)
		{
			if (unit.numberOfScoreablePlayers(scoringRule) > 0)
			{
				scoreableUnits.add(unit);
			}
			else
			{
				remainingUnits.add(unit);
			}
		}
	}

	private List<Unit> sortUnitsByNumberOfScoreablePlayersDescending(List<Unit> units)
	{
		return units
			.stream()
			.sorted(
				Comparator
					.comparing((Unit u) -> u.numberOfScoreablePlayers(scoringRule))
					.reversed())
			.collect(Collectors.toList());
	}

	private List<Unit> sortUnitsByNumberOfPlayersDescending(List<Unit> units)
	{
		return units
			.stream()
			.sorted(Comparator.comparing((Unit u) -> u.numberOfPlayers()).reversed())
			.collect(Collectors.toList());
	}

	private Team getTeamWithLeastScoreablePlayers(List<Team> teams)
	{
		List<Team> sortedTeams = teams
			.stream()
			.sorted(Comparator.comparing((Team t) -> t.numberOfScoreablePlayers(scoringRule)))
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
}
