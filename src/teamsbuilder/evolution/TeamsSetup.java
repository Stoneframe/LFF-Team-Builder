package teamsbuilder.evolution;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import model.Group;
import model.Player;
import model.Team;
import model.Unit;
import model.Group.GroupSplit;
import teamsbuilder.TeamSettings;

public class TeamsSetup
{
	private final Random random = new Random();

	private final List<Team> teams;
	private final FitnessCalculator fitnessCalculator;
	private final TeamSettings settings;

	private Team removeFrom;
	private Team insertInto;

	public TeamsSetup(List<Team> teams, FitnessCalculator fitnessCalculator, TeamSettings settings)
	{
		this.teams = cloneTeams(teams);
		this.fitnessCalculator = fitnessCalculator;
		this.settings = settings;

		mutate();
	}

	public List<Team> getTeams()
	{
		return teams;
	}

	public double getFitness()
	{
		return fitnessCalculator.calculate(teams);
	}

	public List<TeamsSetup> reproduce()
	{
		List<TeamsSetup> setups = new LinkedList<>();

		for (int i = 0; i < 50; i++)
		{
			setups.add(new TeamsSetup(teams, fitnessCalculator, settings));
		}

		return setups;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		builder.append("Fitness: " + getFitness());
		builder.append(System.lineSeparator());

		for (Team team : teams)
		{
			builder.append(
				"Team "
					+ team.getName()
					+ " - "
					+ team.numberOfPlayers()
					+ "("
					+ team.numberOfScoreablePlayers(settings.getScoringRule())
					+ "):");
			builder.append(System.lineSeparator());

			for (Player player : team.getPlayers())
			{
				builder.append(player.getName() + ", " + player.getAge());
				builder.append(System.lineSeparator());
			}

			builder.append(System.lineSeparator());
		}

		return builder.toString();
	}

	private List<Team> cloneTeams(List<Team> teams)
	{
		return teams.stream()
			.map(t -> new Team(t.getName(), t.getUnits()))
			.collect(Collectors.toList());
	}

	private void mutate()
	{
		do
		{
			int mutation = random.nextInt(4);

			switch (mutation)
			{
				case 0:
					selectTeamsWithHighestAndLowestNbrOfPlayers();
					moveNonScoreAbleUnit();
					break;

				case 1:
					selectTeamsWithHighestAndLowestNbrOfScoreAble();
					moveScoreAbleUnit();
					break;

				case 2:
					selectRandomTeams();
					splitAndMoveRandomGroup();
					break;

				case 3:
					selectRandomTeams();
					moveRandomUnit();
					break;
			}
		}
		while (random.nextBoolean());
	}

	private void selectRandomTeams()
	{
		do
		{
			removeFrom = getRandomTeam();
			insertInto = getRandomTeam();
		}
		while (removeFrom == insertInto && removeFrom.numberOfPlayers() == 0);
	}

	private void selectTeamsWithHighestAndLowestNbrOfScoreAble()
	{
		removeFrom = teams.stream().sorted(byNbrOfScoreAble().reversed()).findFirst().get();
		insertInto = teams.stream().sorted(byNbrOfScoreAble()).findFirst().get();
	}

	private void selectTeamsWithHighestAndLowestNbrOfPlayers()
	{
		removeFrom = teams.stream().sorted(byNbrOfPlayers().reversed()).findFirst().get();
		insertInto = teams.stream().sorted(byNbrOfPlayers()).findFirst().get();
	}

	private void moveRandomUnit()
	{
		List<Unit> units = removeFrom.getUnits();

		moveRandomUnit(units);
	}

	private void moveScoreAbleUnit()
	{
		List<Unit> scoreAbleUnits = removeFrom.getUnits()
			.stream()
			.filter(u -> u.numberOfScoreablePlayers(settings.getScoringRule()) > 0)
			.collect(Collectors.toList());

		moveRandomUnit(scoreAbleUnits);
	}

	private void moveNonScoreAbleUnit()
	{
		List<Unit> nonScoreAbleUnits = removeFrom.getUnits()
			.stream()
			.filter(u -> u.numberOfScoreablePlayers(settings.getScoringRule()) == 0)
			.collect(Collectors.toList());

		moveRandomUnit(nonScoreAbleUnits);
	}

	private void splitAndMoveRandomGroup()
	{
		Group group = getLargestGroup(removeFrom);

		if (group != null)
		{
			GroupSplit split = group.split();

			removeFrom.remove(group);
			removeFrom.add(split.getUnit1());

			insertInto.add(split.getUnit2());
		}
	}

	private Group getLargestGroup(Team team)
	{
		return team.getUnits()
			.stream()
			.filter(u -> u instanceof Group)
			.map(u -> (Group)u)
			.filter(g -> !g.isLocked())
			.sorted(byNbrOfPlayers().reversed())
			.findFirst()
			.orElse(null);
	}

	private void moveRandomUnit(List<Unit> units)
	{
		Unit unit = getRandomUnit(units);

		if (unit != null)
		{
			removeFrom.remove(unit);
			insertInto.add(unit);
		}
	}

	private Team getRandomTeam()
	{
		int index = random.nextInt(teams.size());

		return teams.get(index);
	}

	private Unit getRandomUnit(List<Unit> units)
	{
		if (units.size() == 0)
		{
			return null;
		}

		int index = random.nextInt(units.size());

		return units.get(index);
	}

	private Comparator<? super Unit> byNbrOfScoreAble()
	{
		return (unit1, unit2) -> Integer.compare(
			unit1.numberOfScoreablePlayers(settings.getScoringRule()),
			unit2.numberOfScoreablePlayers(settings.getScoringRule()));
	}

	private Comparator<? super Unit> byNbrOfPlayers()
	{
		return (unit1, unit2) -> Integer.compare(unit1.numberOfPlayers(), unit2.numberOfPlayers());
	}
}
