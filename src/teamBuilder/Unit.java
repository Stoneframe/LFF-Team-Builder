package teamBuilder;

import java.util.function.Function;

public interface Unit
{
	public int numberOfPlayers();

	public int numberOfPlayersThatCanScore(Function<Integer, Boolean> scoringRule);
}
