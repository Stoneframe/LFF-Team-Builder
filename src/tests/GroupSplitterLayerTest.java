package tests;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Group;
import model.Player;
import model.Team;
import model.Unit;
import teamsbuilder.Algorithm;
import teamsbuilder.GroupSplitterLayer;
import teamsbuilder.TeamsValidator;
import util.UnitsUtil;

public class GroupSplitterLayerTest
{
	private GroupSplitterLayer groupSplitterLayer;

	@BeforeEach
	public void setUp()
	{
		Function<Integer, Boolean> scoringRule = age -> age < 15 || 50 < age;

		groupSplitterLayer = new GroupSplitterLayer(
				new Algorithm(
						scoringRule,
						Arrays.asList("Team1", "Team2", "Team3", "Team4", "Team5")),
				scoringRule,
				new TeamsValidator(scoringRule, 3, 4));
	}

	@Test
	public void testNoAssert()
	{
		final int playersPerTeam = 3;

		List<Unit> units = UnitsUtil.createRandomUnitList(true);

		int numberOfPlayers = UnitsUtil.countNumberOfPlayers(units);
		int numberOfTeams = numberOfPlayers / playersPerTeam - 1;

		List<Team> teams = groupSplitterLayer.createTeams(units, numberOfTeams);

		UnitsUtil.print(units, teams);
	}

	@Test
	public void test1()
	{
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

		List<Team> teams = groupSplitterLayer.createTeams(units, numberOfTeams);

		UnitsUtil.print(units, teams);
	}
}
