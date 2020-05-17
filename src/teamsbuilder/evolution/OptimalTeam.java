package teamsbuilder.evolution;

public class OptimalTeam
{
	private final double nbrOfScoreAble;
	private final double nbrOfTeenAgers;
	private final double nbrOfPlayers;

	public OptimalTeam(double nbrOfScoreAble, double nbrOfTeenAgers, double nbrOfPlayers)
	{
		this.nbrOfScoreAble = nbrOfScoreAble;
		this.nbrOfTeenAgers = nbrOfTeenAgers;
		this.nbrOfPlayers = nbrOfPlayers;
	}

	public double numberOfScoreAblePlayers()
	{
		return nbrOfScoreAble;
	}

	public double numberOfTeenAgers()
	{
		return nbrOfTeenAgers;
	}

	public double numberOfPlayers()
	{
		return nbrOfPlayers;
	}
}
