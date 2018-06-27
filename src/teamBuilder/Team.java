package teamBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Team
	implements
		Unit
{
	private List<Player> players = new LinkedList<>();

	public void add(Player player)
	{
		players.add(player);
	}

	@Override
	public int numberOfPlayers()
	{
		return players.size();
	}

	@Override
	public int numberOfPlayersThatCanScore(Function<Integer, Boolean> scoringRule)
	{
		return players.stream().mapToInt(p -> p.numberOfPlayersThatCanScore(scoringRule)).sum();
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		for (Player player : players)
		{
			builder.append(player);
			builder.append(System.lineSeparator());
		}
		
		return builder.toString();
	}
}
