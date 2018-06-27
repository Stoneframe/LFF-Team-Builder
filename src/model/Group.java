package model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Group
	implements
		Unit,
		Iterable<Unit>
{
	private List<Unit> units = new LinkedList<>();

	public Group()
	{
	}

	public Group(Unit... units)
	{
		this.units.addAll(Arrays.asList(units));
	}

	public void add(Unit unit)
	{
		units.add(unit);
	}

	public void split(Group group1, Group group2)
	{
		for (int i = 0; i < units.size(); i++)
		{
			if (i % 2 == 0)
			{
				group1.add(units.get(i));
			}
			else
			{
				group2.add(units.get(i));
			}
		}
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
	public Iterator<Unit> iterator()
	{
		return units.iterator();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		for (Unit unit : units)
		{
			builder.append(unit);
			builder.append(System.lineSeparator());
		}

		return builder.toString();
	}
}
