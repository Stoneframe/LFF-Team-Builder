package teamsbuilder;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import model.Team;
import model.Unit;

public class Algorithm
	implements
		Layer
{
	private Function<Integer, Boolean> scoringRule;

	private Comparator<Unit> LEAST_NUMBER_OF_SCOREABLE_PLAYERS =
			Comparator.comparing((Unit u) -> u.numberOfScoreablePlayers(scoringRule));

	private Comparator<Unit> LEAST_NUMBER_OF_PLAYERS =
			Comparator.comparing((Unit u) -> u.numberOfPlayers());

	private Comparator<Unit> MOST_NUMBER_OF_SCOREABLE_PLAYERS =
			LEAST_NUMBER_OF_SCOREABLE_PLAYERS.reversed();

	private Comparator<Unit> MOST_NUMBER_OF_PLAYERS =
			LEAST_NUMBER_OF_PLAYERS.reversed();

	private List<String> teamNames;

	public Algorithm(Function<Integer, Boolean> scoringRule, List<String> teamNames)
	{
		this.scoringRule = scoringRule;
		this.teamNames = teamNames;
	}

	@Override
	public List<Team> createTeams(List<Unit> units, int numberOfTeams)
	{
		System.out.println("Creation of teams started");

		List<Unit> scoreableUnits = new LinkedList<>();
		List<Unit> remainingUnits = new LinkedList<>();

		splitUnitsByScoreablePlayers(units, scoreableUnits, remainingUnits);

		List<Team> teams = createEmptyTeams(numberOfTeams);

		System.out.println("Distributing scorable players");

		for (Unit unit : sortUnitsBy(
			scoreableUnits,
			MOST_NUMBER_OF_SCOREABLE_PLAYERS,
			LEAST_NUMBER_OF_PLAYERS))
		{
			Team team = getTeamWith(
				teams,
				LEAST_NUMBER_OF_SCOREABLE_PLAYERS,
				LEAST_NUMBER_OF_PLAYERS);

			System.out.printf("Adding\n%s\nto\n%s\n", unit, team);

			team.add(unit);
		}

		System.out.println("Distributing remaining players");

		for (Unit unit : sortUnitsBy(remainingUnits, MOST_NUMBER_OF_PLAYERS))
		{
			Team team = getTeamWith(teams, LEAST_NUMBER_OF_PLAYERS);

			System.out.printf("Adding\n%s\nto\n%s\n", unit, team);

			team.add(unit);
		}

		System.out.println("Creation of teams finished");

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

	private List<Team> createEmptyTeams(int numberOfTeams)
	{
		List<Team> teams = new LinkedList<>();

		for (int i = 0; i < numberOfTeams; i++)
		{
			Team team = new Team(teamNames.get(i % teamNames.size()));

			teams.add(team);
		}

		return teams;
	}

	@SafeVarargs
	private final List<Unit> sortUnitsBy(List<Unit> units, Comparator<Unit>... comparators)
	{
		Comparator<Unit> comparator = buildComparator(comparators);

		return units
			.stream()
			.sorted(comparator)
			.collect(Collectors.toList());
	}

	@SafeVarargs
	private final Team getTeamWith(List<Team> teams, Comparator<Unit>... comparators)
	{
		Comparator<Unit> comparator = buildComparator(comparators);

		return teams
			.stream()
			.sorted(comparator)
			.findFirst()
			.get();
	}

	@SafeVarargs
	private final Comparator<Unit> buildComparator(Comparator<Unit>... comparators)
	{
		Comparator<Unit> comparator = comparators[0];

		for (int i = 1; i < comparators.length; i++)
		{
			comparator = comparator.thenComparing(comparators[i]);
		}

		return comparator;
	}

}
