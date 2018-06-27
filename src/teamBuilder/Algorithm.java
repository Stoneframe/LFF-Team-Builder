package teamBuilder;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Algorithm
{
	private Function<Integer, Boolean> scoringRule;

	private int numberOfTeams;

	private Comparator<Unit> NUMBER_OF_SCOREABLE_PLAYERS =
			Comparator.comparing((Unit u) -> u.numberOfScoreablePlayers(scoringRule));

	private Comparator<Unit> NUMBER_OF_PLAYERS =
			Comparator.comparing((Unit u) -> u.numberOfPlayers());

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

		List<Team> teams = createEmptyTeams(numberOfTeams);

		for (Unit unit : sortUnitsBy(scoreableUnits, NUMBER_OF_SCOREABLE_PLAYERS.reversed()))
		{
			Team team = getTeamWithLowest(teams, NUMBER_OF_SCOREABLE_PLAYERS);

			team.add(unit);
		}

		for (Unit unit : sortUnitsBy(remainingUnits, NUMBER_OF_PLAYERS.reversed()))
		{
			Team team = getTeamWithLowest(teams, NUMBER_OF_PLAYERS);

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

	private static List<Team> createEmptyTeams(int numberOfTeams)
	{
		return Stream
			.generate(Team::new)
			.limit(numberOfTeams)
			.collect(Collectors.toList());
	}

	private List<Unit> sortUnitsBy(List<Unit> units, Comparator<Unit> comparator)
	{
		return units
			.stream()
			.sorted(comparator)
			.collect(Collectors.toList());
	}

	private Team getTeamWithLowest(List<Team> teams, Comparator<Unit> comparator)
	{
		return teams
			.stream()
			.sorted(comparator)
			.findFirst()
			.get();
	}
}
