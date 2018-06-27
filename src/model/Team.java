package model;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Team
	implements
		Unit
{
	private List<Player> players = new LinkedList<>();

	private String name;

	public Team(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void add(Player player)
	{
		players.add(player);
	}

	public void add(Unit unit)
	{
		if (unit instanceof Player)
		{
			add((Player)unit);
		}
		else
		{
			for (Unit subUnit : (Group)unit)
			{
				add(subUnit);
			}
		}
	}

	@Override
	public int numberOfPlayers()
	{
		return players.size();
	}

	@Override
	public int numberOfScoreablePlayers(Function<Integer, Boolean> scoringRule)
	{
		return players.stream().mapToInt(p -> p.numberOfScoreablePlayers(scoringRule)).sum();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		builder.append(name + ":");
		builder.append(System.lineSeparator());

		for (Player player : players)
		{
			builder.append(player);
			builder.append(System.lineSeparator());
		}

		return builder.toString();
	}
}
