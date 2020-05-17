package teamsbuilder.evolution;

import java.util.List;

import model.NumberOf;
import model.Team;

public class LffFitnessCalculator
	implements
		FitnessCalculator
{
	private static final double FACTOR = 10;

	private final OptimalTeam optimalTeam;

	public LffFitnessCalculator(OptimalTeam optimalTeam)
	{
		this.optimalTeam = optimalTeam;
	}

	@Override
	public double calculate(List<Team> teams)
	{
		return (teams.stream().mapToDouble(t -> calculate(t)).average().getAsDouble()
			* teamSize(teams));
	}

	private double calculate(Team team)
	{
		return (scoreAble(team) + teenAgers(team) + teamSize(team)) / 3;
	}

	private double scoreAble(Team team)
	{
		double diff = Math.abs(
			optimalTeam.numberOfScoreAblePlayers() - team.count(NumberOf.SCORE_ABLE));

		return FACTOR / (FACTOR + diff);
	}

	private double teenAgers(Team team)
	{
		double diff = Math.abs(
			optimalTeam.numberOfTeenAgers() - team.count(NumberOf.TEEN_AGERS));

		return FACTOR / (FACTOR + diff);
	}

	private double teamSize(Team team)
	{
		double diff = Math.abs(
			optimalTeam.numberOfPlayers() - team.count(NumberOf.PLAYERS));

		return FACTOR / (FACTOR + diff);
	}

	private double teamSize(List<Team> teams)
	{
		int largestTeamSize =
				(int)teams.stream().mapToInt(t -> t.count(NumberOf.PLAYERS)).max().getAsInt();
		int smallestTeamSize =
				(int)teams.stream().mapToInt(t -> t.count(NumberOf.PLAYERS)).min().getAsInt();

		int diff = largestTeamSize - smallestTeamSize;

		return FACTOR / (FACTOR + diff);
	}
}
