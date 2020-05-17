package model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
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
	public List<Player> getPlayers()
	{
		return units.stream().flatMap(u -> u.getPlayers().stream()).collect(Collectors.toList());
	}

	@Override
	public int count(Predicate<Player> predicate)
	{
		return units.stream().mapToInt(u -> u.count(predicate)).sum();
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
