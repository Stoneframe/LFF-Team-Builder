package model;

import java.util.List;
import java.util.function.Function;

public interface Unit
{
	public int numberOfPlayers();

	public int numberOfScoreablePlayers(Function<Integer, Boolean> scoringRule);
	
	public List<Player> getPlayers();
}
