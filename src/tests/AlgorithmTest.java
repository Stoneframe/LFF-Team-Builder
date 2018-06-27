package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import model.Group;
import model.Player;
import model.Team;
import model.Unit;
import teamsbuilder.Algorithm;
import teamsbuilder.Layer;

public class AlgorithmTest
{
	private Function<Integer, Boolean> scoringRule = age -> age < 15;

	private List<String> teamNames = Arrays.asList("Team 1", "Team 2", "Team 3");

	@Test
	public void test1()
	{
		Layer algorithm = new Algorithm(scoringRule, teamNames);

		List<Unit> units = Arrays.asList(
			new Player("Player 1", 10),
			new Player("Player 2", 10),
			new Player("Player 3", 10));

		List<Team> teams = algorithm.createTeams(units, 3);

		assertEquals(3, teams.size());
		assertEquals(1, teams.get(0).numberOfPlayers());
		assertEquals(1, teams.get(1).numberOfPlayers());
		assertEquals(1, teams.get(2).numberOfPlayers());
	}

	@Test
	public void test2()
	{
		Layer algorithm = new Algorithm(scoringRule, teamNames);

		List<Unit> units = Arrays.asList(
			new Group(
					new Player("Player 1", 10),
					new Player("Player 2", 20),
					new Player("Player 3", 20)),
			new Player("Player 4", 10),
			new Player("Player 9", 20),
			new Group(
					new Player("Player 5", 20),
					new Player("Player 6", 10),
					new Player("Player 7", 20)),
			new Group(
					new Player("Player 8", 20)));

		List<Team> teams = algorithm.createTeams(units, 3);

		assertEquals(3, teams.size());
		assertEquals(3, teams.get(0).numberOfPlayers());
		assertEquals(3, teams.get(1).numberOfPlayers());
		assertEquals(3, teams.get(2).numberOfPlayers());
		assertEquals(1, teams.get(0).numberOfScoreablePlayers(scoringRule));
		assertEquals(1, teams.get(1).numberOfScoreablePlayers(scoringRule));
		assertEquals(1, teams.get(2).numberOfScoreablePlayers(scoringRule));
	}

	@Test
	public void testNoAssert()
	{
		final int playersPerTeam = 5;

		List<Unit> units = UnitsGenerator.createRandomUnitList();

		int numberOfPlayers = units.stream().mapToInt(u -> u.numberOfPlayers()).sum();
		int numberOfTeams = numberOfPlayers / playersPerTeam - 1;

		Layer algorithm = new Algorithm(scoringRule, teamNames);

		List<Team> teams = algorithm.createTeams(units, numberOfTeams);

		print(units, teams);
	}

	private static void print(List<Unit> units, List<Team> teams)
	{
		System.out.println("Units:");
		System.out.println();
		units.forEach(u -> System.out.println(u));

		System.out.println();
		System.out.println();

		System.out.println("Teams:");
		System.out.println();
		teams.forEach(t -> System.out.println(t));
	}
}
