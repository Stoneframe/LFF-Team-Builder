package model;

import java.util.function.Function;

public interface Unit
{
	public int numberOfPlayers();

	public int numberOfScoreablePlayers(Function<Integer, Boolean> scoringRule);
}
