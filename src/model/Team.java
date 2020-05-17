package model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Team
	implements
		Unit,
		Iterable<Unit>
{
	private List<Unit> units = new LinkedList<>();

	private String name;

	public Team(String name)
	{
		this.name = name;
	}

	public Team(String name, List<Unit> units)
	{
		this.name = name;
		this.units.addAll(units);
	}

	public String getName()
	{
		return name;
	}

	public void add(Player player)
	{
		units.add(player);
	}

	public void add(Unit unit)
	{
		units.add(unit);
	}

	public void remove(Unit unit)
	{
		units.remove(unit);
	}

	@Override
	public int numberOfPlayers()
	{
		return units.stream().mapToInt(p -> p.numberOfPlayers()).sum();
	}

	@Override
	public int numberOfScoreablePlayers(Function<Integer, Boolean> scoringRule)
	{
		return units.stream().mapToInt(p -> p.numberOfScoreablePlayers(scoringRule)).sum();
	}

	@Override
	public List<Player> getPlayers()
	{
		return units.stream().flatMap(u -> u.getPlayers().stream()).collect(Collectors.toList());
	}

	public List<Unit> getUnits()
	{
		return new LinkedList<>(units);
	}

	@Override
	public Iterator<Unit> iterator()
	{
		return units.iterator();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		builder.append(name + ":");
		builder.append(System.lineSeparator());

		for (Unit unit : units)
		{
			builder.append(unit);
			builder.append(System.lineSeparator());
		}

		return builder.toString();
	}
}
