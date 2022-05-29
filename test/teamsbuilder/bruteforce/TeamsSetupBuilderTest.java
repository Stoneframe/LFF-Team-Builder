package teamsbuilder.bruteforce;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.FileHandler;
import model.Group;
import model.NumberOf;
import model.Player;
import model.Team;
import model.Unit;
import teamsbuilder.TeamSettings;
import teamsbuilder.evolution.TeamsSetupBuilder;

public class TeamsSetupBuilderTest
{
	private static final List<String> TEAM_NAMES =
			Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");

	@Test
	public void threeScoreAblePlayersThreeTeams()
	{
		TeamSettings settings = getTeamSettings(3);

		List<Unit> units = Arrays.asList(
			new Player("1", 12),
			new Player("2", 12),
			new Player("3", 12));

		List<Team> teams = new TeamsSetupBuilder(units, settings).createTeams();

		assertNumberOfTeams(teams, 3);
		assertNumberOfPlayers(teams, 3);
		assertTeamSize(teams);
		assertScoreAblePlayers(teams);
	}

	@Test
	public void threeScoreAblePlayersAndOneNormalPlayerThreeTeams()
	{
		TeamSettings settings = getTeamSettings(3);

		List<Unit> units = Arrays.asList(
			new Player("1", 12),
			new Player("2", 12),
			new Player("3", 12),
			new Player("4", 35));

		List<Team> teams = new TeamsSetupBuilder(units, settings).createTeams();

		assertNumberOfTeams(teams, 3);
		assertNumberOfPlayers(teams, units.size());
		assertTeamSize(teams);
		assertScoreAblePlayers(teams);
	}

	@Test
	public void threeScoreAblePlayersAndNineNormalPlayerThreeTeams()
	{
		TeamSettings settings = getTeamSettings(3);

		List<Unit> units = Arrays.asList(
			new Player("01", 12),
			new Player("02", 12),
			new Player("03", 12),
			new Player("04", 35),
			new Player("05", 35),
			new Player("06", 35),
			new Player("07", 35),
			new Player("08", 35),
			new Player("09", 35),
			new Player("10", 35),
			new Player("11", 35),
			new Player("12", 35));

		List<Team> teams = new TeamsSetupBuilder(units, settings).createTeams();

		assertNumberOfTeams(teams, 3);
		assertNumberOfPlayers(teams, 12);
		assertTeamSize(teams);
		assertScoreAblePlayers(teams);
	}

	@Test
	public void fourGroupsOnePlayerOneGroupNeedsToBeSplitTeams()
	{
		TeamSettings settings = getTeamSettings(3);

		List<Unit> units = Arrays.asList(
			new Group(
				new Player("1", 12),
				new Player("2", 35)),
			new Group(
				new Player("3", 12),
				new Player("4", 35)),
			new Group(
				new Player("5", 12),
				new Player("6", 35)),
			new Group(
				new Player("7", 12),
				new Player("8", 35)),
			new Player("9", 35));

		List<Team> teams = new TeamsSetupBuilder(units, settings).createTeams();

		assertNumberOfTeams(teams, 3);
		assertNumberOfPlayers(teams, 9);
		assertTeamSize(teams);
		assertScoreAblePlayers(teams);
	}

	@Test
	public void largeTest() throws IOException
	{
		TeamSettings settings = getTeamSettings(10);

		List<Unit> units = FileHandler.readFromFile(Paths.get("large_test_input.txt"));

		List<Team> teams = new TeamsSetupBuilder(units, settings).createTeams();

		assertNumberOfTeams(teams, 10);
		assertNumberOfPlayers(teams, 105);
		assertTeamSize(teams);
		assertScoreAblePlayers(teams);
	}

	private TeamSettings getTeamSettings(int numberOfTeams)
	{
		TeamSettings settings = new TeamSettings(TEAM_NAMES);

		settings.setNumberOfTeams(numberOfTeams);

		return settings;
	}

	private static void assertNumberOfTeams(List<Team> teams, int nbrOfTeams)
	{
		assertEquals(nbrOfTeams, teams.size());
	}

	private static void assertNumberOfPlayers(List<Team> teams, int nbrOfPlayers)
	{
		assertEquals(
			nbrOfPlayers,
			teams.stream().flatMap(t -> t.getPlayers().stream()).distinct().count());
	}

	private static void assertTeamSize(List<Team> teams)
	{
		int highest = teams.stream().mapToInt(t -> t.count(NumberOf.PLAYERS)).max().getAsInt();
		int lowest = teams.stream().mapToInt(t -> t.count(NumberOf.PLAYERS)).min().getAsInt();

		assertTrue(highest - lowest < 2);
	}

	private static void assertScoreAblePlayers(List<Team> teams)
	{
		int highest = teams.stream()
			.mapToInt(t -> t.count(NumberOf.SCORE_ABLE))
			.max()
			.getAsInt();
		int lowest = teams.stream()
			.mapToInt(t -> t.count(NumberOf.SCORE_ABLE))
			.min()
			.getAsInt();

		assertTrue(highest - lowest < 2);
	}
}
