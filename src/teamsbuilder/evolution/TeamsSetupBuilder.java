package teamsbuilder.evolution;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import model.Team;
import model.Unit;
import teamsbuilder.TeamSettings;

public class TeamsSetupBuilder
{
	private final Random random = new Random();

	private final List<Unit> units;
	private final TeamSettings settings;

	private final OptimalTeam optimalteam;

	private final List<TeamsSetup> setups = new LinkedList<>();

	public TeamsSetupBuilder(List<Unit> units, TeamSettings settings)
	{
		this.units = units;
		this.settings = settings;

		this.optimalteam = createOptimalTeam();
	}

	public List<Team> createTeams()
	{
		initalizeRandomSetup();

		for (int i = 0; i < 100; i++)
		{
			sortByFitness();
			cullTheWeak();
			reproduce();
		}

		return getTeams();
	}

	private boolean initalizeRandomSetup()
	{
		return setups.add(createRandomTeamsSetup());
	}

	private TeamsSetup createRandomTeamsSetup()
	{
		List<Team> teams = createRandomTeams();

		return new TeamsSetup(
			teams,
			new LffFitnessCalculator(settings.getScoringRule(), optimalteam),
			settings);
	}

	private List<Team> createRandomTeams()
	{
		List<Team> teams = createEmptyTeams();

		units.forEach(unit -> addUnitToRandomTeam(unit, teams));

		return teams;
	}

	private void addUnitToRandomTeam(Unit unit, List<Team> teams)
	{
		teams.get(random.nextInt(teams.size())).add(unit);
	}

	private List<Team> createEmptyTeams()
	{
		List<Team> teams = new LinkedList<>();

		for (int i = 0; i < settings.getNumberOfTeams(); i++)
		{
			teams.add(new Team(settings.getTeamNames().get(i)));
		}

		return teams;
	}

	private void sortByFitness()
	{
		setups.sort(byHighestFitness());
	}

	private Comparator<? super TeamsSetup> byHighestFitness()
	{
		return (s1, s2) -> Double.compare(s2.getFitness(), s1.getFitness());
	}

	private void cullTheWeak()
	{
		int size = setups.size();

		for (int i = 0; i < size - 5; i++)
		{
			setups.remove(5);
		}
	}

	private void reproduce()
	{
		int size = setups.size();

		for (int i = 0; i < size; i++)
		{
			setups.addAll(setups.get(i).reproduce());
		}
	}

	private List<Team> getTeams()
	{
		return setups.get(0).getTeams();
	}

	private OptimalTeam createOptimalTeam()
	{
		return new OptimalTeam(
			getAverageNbrOfScoreAble(),
			getAverageNbrOfTeenAgers(),
			getAverageNbrOfPlayers());
	}

	private double getAverageNbrOfScoreAble()
	{
		return units.stream()
			.mapToDouble(u -> u.numberOfScoreablePlayers(settings.getScoringRule()))
			.sum()
			/ settings.getNumberOfTeams();
	}

	private double getAverageNbrOfTeenAgers()
	{
		return units.stream()
			.mapToDouble(u -> nbrOfTeenAgers(u))
			.sum()
			/ settings.getNumberOfTeams();
	}

	private double getAverageNbrOfPlayers()
	{
		return units.stream()
			.mapToDouble(u -> u.numberOfPlayers())
			.sum()
			/ settings.getNumberOfTeams();
	}

	private int nbrOfTeenAgers(Unit unit)
	{
		return (int)unit.getPlayers()
			.stream()
			.filter(p -> 12 < p.getAge() && p.getAge() < 20)
			.count();
	}
}
