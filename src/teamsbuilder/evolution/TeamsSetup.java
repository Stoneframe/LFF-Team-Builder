package teamsbuilder.evolution;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import model.Group;
import model.Group.GroupSplit;
import model.NumberOf;
import model.NumberOf.Category;
import model.Team;
import model.Unit;

public class TeamsSetup
{
	private final Random random = new Random();

	private final List<Team> teams;
	private final FitnessCalculator fitnessCalculator;

	private final Category[] categories;

	private Team team1;
	private Team team2;

	private Unit unit1;
	private Unit unit2;

	public TeamsSetup(List<Team> teams, FitnessCalculator fitnessCalculator, Category[] categories)
	{
		this.teams = cloneTeams(teams);
		this.fitnessCalculator = fitnessCalculator;

		this.categories = categories;

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

		for (int i = 0; i < 100; i++)
		{
			setups.add(new TeamsSetup(teams, fitnessCalculator, categories));
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

		return builder.toString();
	}

	private String toString(Team team)
	{
		int nbrOfPlayers = team.count(NumberOf.PLAYERS);
		int nbrOfScoreAble = team.count(NumberOf.SCORE_ABLE);
		int nbrOfTeenAgers = team.count(NumberOf.TEEN_AGERS);
		int nbrOfYounglings = team.count(NumberOf.YOUNG_CHILDREN);

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
			int mutation = random.nextInt(2 + categories.length);

			switch (mutation)
			{
				case 0:
					splitGroupMutation();
					break;

				case 1:
					randomUnitMutation();
					break;

				default:
					mutate(categories[mutation - 2]);
			}
		}
		while (random.nextBoolean());
	}

	private void mutate(Category category)
	{
		selectTeamsWithHighestAndLowest(category);

		List<Unit> units = team1.getUnits()
			.stream()
			.filter(u -> u.count(category) > 0)
			.collect(Collectors.toList());

		unit1 = getRandomUnit(units);
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

	private void selectTeamsWithHighestAndLowest(Category category)
	{
		team1 = teams.stream().sorted(by(category).reversed()).findFirst().get();
		team2 = teams.stream().sorted(by(category)).findFirst().get();
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
			.sorted(by(NumberOf.PLAYERS).reversed())
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

	private Comparator<? super Unit> by(Category category)
	{
		return (unit1, unit2) -> Integer.compare(unit1.count(category), unit2.count(category));
	}
}
