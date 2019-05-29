package tests;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Team;
import model.Unit;
import teamsbuilder.Algorithm;
import teamsbuilder.GroupSplitterLayer;
import teamsbuilder.TeamsValidator;

public class GroupSplitterLayerTest
{
	private GroupSplitterLayer groupSplitterLayer;

	@BeforeEach
	public void setUp()
	{
		Function<Integer, Boolean> scoringRule = age -> age < 15;

		groupSplitterLayer = new GroupSplitterLayer(
				new Algorithm(scoringRule, Arrays.asList("Team")),
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
}
