package model;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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

	@Override
	public int numberOfPlayers()
	{
		return 1;
	}

	@Override
	public int numberOfScoreablePlayers(Function<Integer, Boolean> scoringRule)
	{
		return scoringRule.apply(age) ? 1 : 0;
	}

	@Override
	public List<Player> getPlayers()
	{
		return Arrays.asList(this);
	}

	@Override
	public String toString()
	{
		return String.format("%s (%d)", name, age);
	}
}
