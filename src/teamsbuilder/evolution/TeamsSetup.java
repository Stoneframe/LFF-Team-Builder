package teamsbuilder.evolution;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import model.Group;
import model.NumberOf;
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
					+ team.count(NumberOf.PLAYERS)
					+ "("
					+ team.count(NumberOf.SCORE_ABLE)
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
			int mutation = random.nextInt(5);

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
					selectTeamsWithHighestAndLowestNbrOfTeenAgers();
					moveTeenAgersUnit();
					break;

				case 3:
					selectRandomTeams();
					splitAndMoveRandomGroup();
					break;

				case 4:
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
		while (removeFrom == insertInto && removeFrom.count(NumberOf.PLAYERS) == 0);
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

	private void selectTeamsWithHighestAndLowestNbrOfTeenAgers()
	{
		removeFrom = teams.stream().sorted(byNbrOfTeenAgers().reversed()).findFirst().get();
		insertInto = teams.stream().sorted(byNbrOfTeenAgers()).findFirst().get();
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
			.filter(u -> u.count(NumberOf.SCORE_ABLE) > 0)
			.collect(Collectors.toList());

		moveRandomUnit(scoreAbleUnits);
	}

	private void moveNonScoreAbleUnit()
	{
		List<Unit> nonScoreAbleUnits = removeFrom.getUnits()
			.stream()
			.filter(u -> u.count(NumberOf.SCORE_ABLE) == 0)
			.collect(Collectors.toList());

		moveRandomUnit(nonScoreAbleUnits);
	}

	private void moveTeenAgersUnit()
	{
		List<Unit> teenAgersUnits = removeFrom.getUnits()
			.stream()
			.filter(u -> nbrOfTeenAgers(u) == 0)
			.collect(Collectors.toList());

		moveRandomUnit(teenAgersUnits);
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
			unit1.count(NumberOf.SCORE_ABLE),
			unit2.count(NumberOf.SCORE_ABLE));
	}

	private Comparator<? super Unit> byNbrOfPlayers()
	{
		return (unit1, unit2) -> Integer.compare(
			unit1.count(NumberOf.PLAYERS),
			unit2.count(NumberOf.PLAYERS));
	}

	private Comparator<? super Unit> byNbrOfTeenAgers()
	{
		return (unit1, unit2) -> Integer.compare(nbrOfTeenAgers(unit1), nbrOfTeenAgers(unit2));
	}

	private int nbrOfTeenAgers(Unit unit)
	{
		return (int)unit.getPlayers()
			.stream()
			.filter(p -> 12 < p.getAge() && p.getAge() < 20)
			.count();
	}
}
