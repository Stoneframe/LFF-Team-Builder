package teamsbuilder.evolution;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import model.Group;
import model.Group.GroupSplit;
import model.Player;
import model.Team;
import model.Unit;
import teamsbuilder.TeamSettings;

public class TeamsSetupBuilder
{
	private final Random random = new Random();

	private final List<Unit> units;
	private final TeamSettings settings;

	private final OptimalTeam optimalteam;

	public TeamsSetupBuilder(List<Unit> units, TeamSettings settings)
	{
		this.units = units;
		this.settings = settings;

		double nbrOfScoreAble = units.stream()
			.mapToDouble(u -> u.numberOfScoreablePlayers(settings.getScoringRule()))
			.sum()
			/ settings.getNumberOfTeams();

		double nbrOfTeenAgers = units.stream()
			.mapToDouble(u -> nbrOfTeenAgers(u))
			.sum()
			/ settings.getNumberOfTeams();

		double nbrOfPlayers = units.stream()
			.mapToDouble(u -> u.numberOfPlayers())
			.sum()
			/ settings.getNumberOfTeams();

		this.optimalteam = new OptimalTeam(nbrOfScoreAble, nbrOfTeenAgers, nbrOfPlayers);
	}

	public List<Team> createTeams()
	{
		List<TeamsSetup> setups = new LinkedList<>();

		for (int i = 0; i < 1; i++)
		{
			setups.add(createRandomTeamsSetup(units, settings));
		}

		for (int i = 0; i < 100; i++)
		{
			setups.sort((s1, s2) -> Double.compare(s2.getFitness(), s1.getFitness()));

			int size1 = setups.size();
			for (int j = 0; j < size1 - 5; j++)
			{
				setups.remove(5);
			}

			int size2 = setups.size();
			for (int j = 0; j < size2; j++)
			{
				setups.addAll(setups.get(j).reproduce());
			}
		}

		return setups.get(0).getTeams();
	}

	private TeamsSetup createRandomTeamsSetup(List<Unit> units, TeamSettings settings)
	{
		List<Team> teams = createRandomTeams(units, settings);

		return new TeamsSetup(
			teams,
			new LffFitnessCalculator(settings.getScoringRule(), optimalteam),
			settings);
	}

	private List<Team> createEmptyTeams(TeamSettings settings)
	{
		List<Team> teams = new LinkedList<>();

		for (int i = 0; i < settings.getNumberOfTeams(); i++)
		{
			teams.add(new Team(settings.getTeamNames().get(i)));
		}

		return teams;
	}

	private List<Team> createRandomTeams(List<Unit> units, TeamSettings settings)
	{
		List<Team> teams = createEmptyTeams(settings);

		units.forEach(unit -> addUnitToRandomTeam(unit, teams));

		return teams;
	}

	private void addUnitToRandomTeam(Unit unit, List<Team> teams)
	{
		if (unit instanceof Group)
		{
			addGroupToRandomTeam((Group)unit, teams);
		}
		else
		{
			addPlayerToRandomTeam((Player)unit, teams);
		}
	}

	private void addGroupToRandomTeam(Group group, List<Team> teams)
	{
		if (!group.isLocked() && random.nextDouble() < 0.1)
		{
			GroupSplit split = group.split();

			addUnitToRandomTeam(split.getUnit1(), teams);
			addUnitToRandomTeam(split.getUnit2(), teams);
		}
		else
		{
			Team randomTeam = teams.get(random.nextInt(teams.size()));

			randomTeam.add(group);
		}
	}

	private void addPlayerToRandomTeam(Player player, List<Team> teams)
	{
		Team randomTeam = teams.get(random.nextInt(teams.size()));

		randomTeam.add(player);
	}

	private int nbrOfTeenAgers(Unit unit)
	{
		return (int)unit.getPlayers()
			.stream()
			.filter(p -> 12 < p.getAge() && p.getAge() < 20)
			.count();
	}
}
