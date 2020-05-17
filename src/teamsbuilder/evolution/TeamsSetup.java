package teamsbuilder.evolution;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import model.Group;
import model.Group.GroupSplit;
import model.NumberOf;
import model.Team;
import model.Unit;
import teamsbuilder.TeamSettings;

public class TeamsSetup
{
	private final Random random = new Random();

	private final List<Team> teams;
	private final FitnessCalculator fitnessCalculator;
	private final TeamSettings settings;

	private Team team1;
	private Team team2;

	private Unit unit1;
	private Unit unit2;

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
		builder.append(" (");
		builder.append(teams.stream().map(t -> toString(t)).collect(Collectors.joining(", ")));
		builder.append(")");

		// builder.append(System.lineSeparator());
		//
		// for (Team team : teams)
		// {
		// builder.append(
		// "Team "
		// + team.getName()
		// + " - "
		// + team.count(NumberOf.PLAYERS)
		// + "("
		// + team.count(NumberOf.SCORE_ABLE)
		// + "):");
		// builder.append(System.lineSeparator());
		//
		// for (Player player : team.getPlayers())
		// {
		// builder.append(player.getName() + ", " + player.getAge());
		// builder.append(System.lineSeparator());
		// }
		//
		// builder.append(System.lineSeparator());
		// }

		return builder.toString();
	}

	private String toString(Team team)
	{
		int nbrOfPlayers = team.count(NumberOf.PLAYERS);
		int nbrOfScoreAble = team.count(NumberOf.SCORE_ABLE);
		int nbrOfTeenAgers = team.count(NumberOf.TEEN_AGERS);
		int nbrOfYounglings = team.count(NumberOf.YOUNGLINGS);

		return "("
			+ nbrOfPlayers
			+ ", "
			+ nbrOfScoreAble
			+ ", "
			+ nbrOfTeenAgers
			+ ", "
			+ nbrOfYounglings
			+ ")";
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
					nonScoreAbleMutation();
					break;

				case 1:
					scoreAbleMutation();
					break;

				case 2:
					teenAgersMutation();
					break;

				case 3:
					splitGroupMutation();
					break;

				case 4:
					randomUnitMutation();
					break;
			}
		}
		while (random.nextBoolean());
	}

	private void nonScoreAbleMutation()
	{
		selectTeamsWithHighestAndLowestNbrOfPlayers();

		List<Unit> nonScoreAbleUnits = team1.getUnits()
			.stream()
			.filter(u -> u.count(NumberOf.SCORE_ABLE) == 0)
			.collect(Collectors.toList());

		unit1 = getRandomUnit(nonScoreAbleUnits);
		unit2 = getRandomUnit(team2.getUnits());

		moveUnits();
	}

	private void scoreAbleMutation()
	{
		selectTeamsWithHighestAndLowestNbrOfScoreAble();

		List<Unit> scoreAbleUnits = team1.getUnits()
			.stream()
			.filter(u -> u.count(NumberOf.SCORE_ABLE) > 0)
			.collect(Collectors.toList());

		unit1 = getRandomUnit(scoreAbleUnits);
		unit2 = getRandomUnit(team2.getUnits());

		moveUnits();
	}

	private void teenAgersMutation()
	{
		selectTeamsWithHighestAndLowestNbrOfTeenAgers();

		List<Unit> teenAgersUnits = team1.getUnits()
			.stream()
			.filter(u -> nbrOfTeenAgers(u) == 0)
			.collect(Collectors.toList());

		unit1 = getRandomUnit(teenAgersUnits);
		unit2 = getRandomUnit(team2.getUnits());

		moveUnits();
	}

	private void splitGroupMutation()
	{
		selectRandomTeams();

		Group group = getLargestGroup(team1);

		if (group != null)
		{
			GroupSplit split = group.split();

			team1.remove(group);
			team1.add(split.getUnit1());
			team1.add(split.getUnit2());

			unit1 = split.getUnit2();
			unit2 = getRandomUnit(team2.getUnits());

			moveUnits();
		}
	}

	private void randomUnitMutation()
	{
		selectRandomTeams();

		unit1 = getRandomUnit(team1.getUnits());
		unit2 = getRandomUnit(team2.getUnits());

		moveUnits();
	}

	private void selectTeamsWithHighestAndLowestNbrOfPlayers()
	{
		team1 = teams.stream().sorted(byNbrOfPlayers().reversed()).findFirst().get();
		team2 = teams.stream().sorted(byNbrOfPlayers()).findFirst().get();
	}

	private void selectTeamsWithHighestAndLowestNbrOfScoreAble()
	{
		team1 = teams.stream().sorted(byNbrOfScoreAble().reversed()).findFirst().get();
		team2 = teams.stream().sorted(byNbrOfScoreAble()).findFirst().get();
	}

	private void selectTeamsWithHighestAndLowestNbrOfTeenAgers()
	{
		team1 = teams.stream().sorted(byNbrOfTeenAgers().reversed()).findFirst().get();
		team2 = teams.stream().sorted(byNbrOfTeenAgers()).findFirst().get();
	}

	private void selectRandomTeams()
	{
		do
		{
			team1 = getRandomTeam();
			team2 = getRandomTeam();
		}
		while (team1 == team2 || team1.count(NumberOf.PLAYERS) == 0);
	}

	private Team getRandomTeam()
	{
		int index = random.nextInt(teams.size());

		return teams.get(index);
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

	private void moveUnits()
	{
		if (unit1 != null)
		{
			team1.remove(unit1);
			team2.add(unit1);

			if (unit2 != null && random.nextBoolean())
			{
				team2.remove(unit2);
				team1.add(unit2);
			}
		}
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
