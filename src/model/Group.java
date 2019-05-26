package model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Group
	implements
		Unit,
		Iterable<Player>
{
	private List<Player> players = new LinkedList<>();

	private boolean isLocked;

	public Group(boolean isLocked, List<Player> players)
	{
		this.isLocked = isLocked;
		this.players.addAll(players);
	}

	public Group(boolean isLocked, Player... units)
	{
		this.isLocked = isLocked;
		this.players.addAll(Arrays.asList(units));
	}

	public Group(Player... units)
	{
		this(false, units);
	}

	public boolean isLocked()
	{
		return isLocked;
	}

	public void setLocked(boolean isLocked)
	{
		this.isLocked = isLocked;
	}

	public void add(Player player)
	{
		players.add(player);
	}

	public void split(Group group1, Group group2)
	{
		for (int i = 0; i < players.size(); i++)
		{
			if (i % 2 == 0)
			{
				group1.add(players.get(i));
			}
			else
			{
				group2.add(players.get(i));
			}
		}
	}

	@Override
	public int numberOfPlayers()
	{
		return players.stream().mapToInt(p -> p.numberOfPlayers()).sum();
	}

	@Override
	public int numberOfScoreablePlayers(Function<Integer, Boolean> scoringRule)
	{
		return players.stream().mapToInt(p -> p.numberOfScoreablePlayers(scoringRule)).sum();
	}

	@Override
	public Iterator<Player> iterator()
	{
		return players.iterator();
	}

	@Override
	public String toString()
	{
		return String.join(
			System.lineSeparator(),
			players.stream().map(p -> p.toString()).collect(Collectors.toList()));
	}

	public String toDetailedString()
	{
		StringBuilder builder = new StringBuilder();

		builder.append(isLocked ? "(locked)" : "(not locked)");

		for (Unit unit : players)
		{
			builder.append(System.lineSeparator());
			builder.append(unit);
		}

		return builder.toString();
	}
}
