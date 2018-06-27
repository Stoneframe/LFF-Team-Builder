package teamBuilder;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Algorithm
{
	private int numberOfTeams;
	private int playersPerTeam;

	private Function<Integer, Boolean> scoringRule;

	public Algorithm(int numberOfTeams, int playersPerTeam, Function<Integer, Boolean> scoringRule)
	{
		this.numberOfTeams = numberOfTeams;
		this.playersPerTeam = playersPerTeam;
		this.scoringRule = scoringRule;
	}

	public List<Team> createTeams(List<Unit> units)
	{
		List<Team> teams = createEmptyTeams();

		List<Unit> sortedUnits = sortUnitsByNumberOfPlayersThatCanScore(units);

		for (Unit unit : sortedUnits)
		{
			Team team = getTeamWithLeastPlayersThatCanScore(teams);

			addUnitToTeam(unit, team);
		}

		return teams;
	}

	private List<Team> createEmptyTeams()
	{
		List<Team> teams = new LinkedList<>();

		for (int i = 0; i < numberOfTeams; i++)
		{
			teams.add(new Team());
		}

		return teams;
	}

	private List<Unit> sortUnitsByNumberOfPlayersThatCanScore(List<Unit> units)
	{
		return units
			.stream()
			.sorted(
				Comparator
					.comparing((Unit u) -> u.numberOfPlayersThatCanScore(scoringRule))
					.reversed()
					.thenComparing((Unit u) -> u.numberOfPlayers()))
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
			.filter(u -> u.numberOfPlayers() <= playersPerTeam)
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
