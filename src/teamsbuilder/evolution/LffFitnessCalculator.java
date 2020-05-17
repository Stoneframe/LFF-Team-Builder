package teamsbuilder.evolution;

import java.util.List;
import java.util.function.Function;

import model.Team;

public class LffFitnessCalculator
	implements
		FitnessCalculator
{
	private static final double FACTOR = 1;

	private final Function<Integer, Boolean> scoringRule;
	private final OptimalTeam optimalTeam;

	public LffFitnessCalculator(Function<Integer, Boolean> scoringRule, OptimalTeam optimalTeam)
	{
		this.scoringRule = scoringRule;
		this.optimalTeam = optimalTeam;
	}

	@Override
	public double calculate(List<Team> teams)
	{
		return (teams.stream().mapToDouble(t -> calculate(t)).average().getAsDouble()
			* teamSize(teams)) / 2;
	}

	private double calculate(Team team)
	{
		return (scoreAble(team) + teenAgers(team) + teamSize(team)) / 3;
	}

	private double scoreAble(Team team)
	{
		double diff = Math.abs(
			optimalTeam.numberOfScoreAblePlayers() - team.numberOfScoreablePlayers(scoringRule));

		return FACTOR / (FACTOR + diff);
	}

	private double teenAgers(Team team)
	{
		double diff = Math.abs(
			optimalTeam.numberOfScoreAblePlayers() - nbrOfTeenAgers(team));

		return FACTOR / (FACTOR + diff);
	}

	private int nbrOfTeenAgers(Team team)
	{
		return (int)team.getPlayers()
			.stream()
			.filter(p -> 12 < p.getAge() && p.getAge() < 20)
			.count();
	}

	private double teamSize(Team team)
	{
		return FACTOR / (FACTOR + Math.abs(optimalTeam.numberOfPlayers() - team.numberOfPlayers()));
	}

	private double teamSize(List<Team> teams)
	{
		int largestTeamSize =
				(int)teams.stream().mapToInt(t -> t.numberOfPlayers()).max().getAsInt();
		int smallestTeamSize =
				(int)teams.stream().mapToInt(t -> t.numberOfPlayers()).min().getAsInt();

		int diff = largestTeamSize - smallestTeamSize;

		return FACTOR / (FACTOR + diff);
	}
}
