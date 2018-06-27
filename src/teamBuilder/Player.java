package teamBuilder;

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

	@Override
	public int numberOfPlayers()
	{
		return 1;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public int numberOfPlayersThatCanScore(Function<Integer, Boolean> scoringRule)
	{
		return scoringRule.apply(age) ? 1 : 0;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s (%d)", name, age);
	}
}
