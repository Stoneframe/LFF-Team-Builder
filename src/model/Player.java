package model;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Player
	implements
		Unit
{
	private String name;
	private int age;

	public Player(String name, int age)
	{
		this.name = name;
		this.age = age;
	}

	public String getName()
	{
		return name;
	}

	public int getAge()
	{
		return age;
	}

	public boolean isScoreAble()
	{
		return count(NumberOf.SCORE_ABLE) == 1;
	}

	@Override
	public int count(Predicate<Player> predicate)
	{
		return predicate.test(this) ? 1 : 0;
	}

	@Override
	public List<Player> getPlayers()
	{
		return Arrays.asList(this);
	}

	@Override
	public int hashCode()
	{
		return name.hashCode() ^ Integer.hashCode(age);
	}

	@Override
	public String toString()
	{
		return String.format("%s (%d)", name, age);
	}
}
