package model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
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

	public Group(List<Player> players)
	{
		this(false, players);
	}

	public Group(Player... players)
	{
		this(false, players);
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

	public GroupSplit split()
	{
		if (isLocked) throw new IllegalStateException("Group is locked");

		List<Player> players1 = new LinkedList<>();
		List<Player> players2 = new LinkedList<>();

		for (int i = 0; i < players.size(); i++)
		{
			if (i % 2 == 0)
			{
				players1.add(players.get(i));
			}
			else
			{
				players2.add(players.get(i));
			}
		}

		Function<List<Player>, Unit> converter = (players) ->
			{
				return players.size() == 1
						? players.get(0)
						: new Group(players);
			};

		Unit unit1 = converter.apply(players1);
		Unit unit2 = converter.apply(players2);

		return new GroupSplit(unit1, unit2);
	}

	@Override
	public int count(Predicate<Player> predicate)
	{
		return players.stream().mapToInt(p -> p.count(predicate)).sum();
	}

	@Override
	public List<Player> getPlayers()
	{
		return players;
	}

	@Override
	public Iterator<Player> iterator()
	{
		return players.iterator();
	}

	@Override
	public int hashCode()
	{
		return Boolean.hashCode(isLocked) ^ players.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Group)
		{
			Group other = (Group)obj;

			return this.isLocked == other.isLocked
				&& this.players.equals(other.players);
		}

		return false;
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

	public class GroupSplit
	{
		private final Unit unit1;
		private final Unit unit2;

		public GroupSplit(Unit unit1, Unit unit2)
		{
			this.unit1 = unit1;
			this.unit2 = unit2;
		}

		public Unit getUnit1()
		{
			return unit1;
		}

		public Unit getUnit2()
		{
			return unit2;
		}
	}
}
