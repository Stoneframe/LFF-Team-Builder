package teamBuilder;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TeamsBuilder
{
	private int playersPerTeam;

	private Function<Integer, Boolean> scoringRule;

	public TeamsBuilder(int playersPerTeam, Function<Integer, Boolean> scoringRule)
	{
		this.playersPerTeam = playersPerTeam;
		this.scoringRule = scoringRule;
	}

	public List<Team> createTeams(List<Unit> units)
	{
		int numberOfTeams = countNumberOfPlayers(units) / playersPerTeam;

		List<Team> teams = null;

		for (int n = numberOfTeams; n > 1; n--)
		{
			teams = createTeams(units, n);

			if (!anyTeamTooSmall(teams))
			{
				return teams;
			}
		}

		// List<Team> teams = createTeams(units, numberOfTeams, playersPerTeam);
		//
		// if (anyTeamTooSmall(teams))
		// {
		// return createTeams(units, --numberOfTeams, playersPerTeam);
		// }

		return teams;
	}

	private List<Team> createTeams(List<Unit> units, int numberOfTeams)
	{
		Algorithm algorithm = new Algorithm(numberOfTeams, scoringRule);

		List<Team> teams = algorithm.createTeams(units);

		if (anyTeamInvalid(teams))
		{
			List<Unit> modifiedUnits = modifyUnits(units);

			if (modifiedUnits.size() != units.size())
			{
				return createTeams(modifiedUnits, numberOfTeams);
			}

			assert modifiedUnits.stream().allMatch(u -> u.numberOfPlayers() == 1);
		}

		// if (anyTeamTooSmall(teams) && --numberOfTeams > 0)
		// {
		// return createTeams2(units, numberOfTeams, playersPerTeam);
		// }

		return teams;
	}

	private boolean anyTeamTooLarge(List<Team> teams)
	{
		return teams.stream().anyMatch(t -> t.numberOfPlayers() > playersPerTeam);
	}

	private boolean anyTeamTooSmall(List<Team> teams)
	{
		return teams.stream().anyMatch(t -> t.numberOfPlayers() < playersPerTeam);
	}

	public List<Team> createTeams3(List<Unit> units)
	{
		int numberOfTeams = countNumberOfPlayers(units) / playersPerTeam;

		List<Team> teams = createTeams3(units, numberOfTeams);

		if (!teamSizeDifferToMuch(teams))
		{
			List<Unit> modifyUnits = modifyUnits(units);

			if (modifyUnits.size() != units.size())
			{
				return createTeams(modifyUnits);
			}
		}

		return teams;
	}

	public List<Team> createTeams3(List<Unit> units, int numberOfTeams)
	{
		Algorithm algorithm = new Algorithm(numberOfTeams, scoringRule);

		List<Team> teams = algorithm.createTeams(units);

		if (!anyTeamInvalid(teams) && --numberOfTeams > 1)
		{
			return createTeams3(units, numberOfTeams);
		}

		return teams;
	}

	private static int countNumberOfPlayers(List<Unit> units)
	{
		return units.stream().mapToInt(u -> u.numberOfPlayers()).sum();
	}

	private boolean anyTeamInvalid(List<Team> teams)
	{
		return anyTeamTooSmall(teams) || anyTeamTooLarge(teams);
	}

	private boolean anyTeamBelowPlayersPerTeam(List<Team> teams)
	{
		return teams.stream().anyMatch(t -> t.numberOfPlayers() < playersPerTeam);
	}

	private boolean teamSizeDifferToMuch(List<Team> teams)
	{
		int nbrOfPlayersInLargestTeam =
				teams.stream().mapToInt(t -> t.numberOfPlayers()).max().getAsInt();

		int nbrOfPlayersInSmallestTeam =
				teams.stream().mapToInt(t -> t.numberOfPlayers()).min().getAsInt();

		return nbrOfPlayersInLargestTeam - nbrOfPlayersInSmallestTeam > 3;
	}

	private List<Unit> modifyUnits(List<Unit> units)
	{
		List<Unit> sortedUnits = units
			.stream()
			.sorted(Comparator.comparing((Unit u) -> u.numberOfPlayers()).reversed())
			.collect(Collectors.toList());

		List<Unit> modifiedUnits = new LinkedList<>();

		for (int i = 0; i < sortedUnits.size(); i++)
		{
			if (i == 0 && sortedUnits.get(i).numberOfPlayers() > 1)
			{
				Group group = (Group)sortedUnits.get(i);

				Group[] split = group.split();

				modifiedUnits.add(split[0]);
				modifiedUnits.add(split[1]);
			}
			else
			{
				modifiedUnits.add(sortedUnits.get(i));
			}
		}

		return modifiedUnits;
	}
}
