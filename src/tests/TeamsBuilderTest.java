package tests;

import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import teamBuilder.Team;
import teamBuilder.TeamsBuilder;
import teamBuilder.Unit;

public class TeamsBuilderTest
{
	final private Function<Integer, Boolean> scoringRule = age -> age < 15;

	@Test
	public void test()
	{
		final int playersPerTeam = 5;

		List<Unit> units = UnitsGenerator.createRandomUnitList();

		TeamsBuilder teamsBuilder = new TeamsBuilder(playersPerTeam, scoringRule);

		List<Team> teams = teamsBuilder.createTeams(units);

		print(units, teams);
	}

	private static void print(List<Unit> units, List<Team> teams)
	{
		System.out.println("Units:");
		units.forEach(u -> System.out.println(u));

		System.out.println();
		System.out.println();

		System.out.println("Teams:");
		teams.forEach(t -> System.out.println(t));
	}
}
