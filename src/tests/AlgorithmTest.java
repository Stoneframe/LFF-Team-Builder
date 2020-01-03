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
import teamsbuilder.scorableonly.UnitDividerLayer;
import util.UnitsUtil;

public class AlgorithmTest
{
	private Function<Integer, Boolean> scoringRule = age -> age < 15;

	private List<String> teamNames = Arrays.asList("Team 1", "Team 2", "Team 3");

	@Test
	public void test1()
	{
		UnitDividerLayer algorithm = new UnitDividerLayer(scoringRule, teamNames);

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
		UnitDividerLayer algorithm = new UnitDividerLayer(scoringRule, teamNames);

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
	public void test3()
	{
		UnitDividerLayer algorithm = new UnitDividerLayer(scoringRule, teamNames);

		List<Unit> units = Arrays.asList(
			new Player("Player_1_0", 60),
			new Player("Player_2_0", 11),
			new Player("Player_3_0", 13),
			new Group(
				new Player("Player_4_1", 29),
				new Player("Player_5_1", 31)),
			new Player("Player_6_0", 26),
			new Player("Player_7_0", 29),
			new Group(
				new Player("Player_8_2", 40),
				new Player("Player_9_2", 26)));

		int numberOfTeams = 3;

		List<Team> teams = algorithm.createTeams(units, numberOfTeams);

		UnitsUtil.print(units, teams);
	}

	@Test
	public void testNoAssert()
	{
		final int playersPerTeam = 5;

		List<Unit> units = UnitsUtil.createRandomUnitList();

		int numberOfPlayers = UnitsUtil.countNumberOfPlayers(units);
		int numberOfTeams = numberOfPlayers / playersPerTeam - 1;

		UnitDividerLayer algorithm = new UnitDividerLayer(scoringRule, teamNames);

		List<Team> teams = algorithm.createTeams(units, numberOfTeams);

		UnitsUtil.print(units, teams);
	}
}
