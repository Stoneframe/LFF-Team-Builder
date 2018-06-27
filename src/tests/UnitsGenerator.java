package tests;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import teamBuilder.Group;
import teamBuilder.Player;
import teamBuilder.Unit;

public class UnitsGenerator
{
	private static int groupCount = 0;
	private static int playerCount = 0;

	public static List<Unit> createRandomUnitList()
	{
		List<Unit> units = new LinkedList<>();

		int numberOfUnits = new Random().nextInt(20) + 10;

		for (int i = 0; i < numberOfUnits; i++)
		{
			units.add(UnitsGenerator.createRandomUnit());
		}

		return units;
	}

	public static Unit createRandomUnit()
	{
		if (new Random().nextBoolean())
		{
			return UnitsGenerator.createRandomPlayer(0);
		}
		else
		{
			return UnitsGenerator.createRandomGroup();
		}
	}

	public static Group createRandomGroup()
	{
		int numberOfPlayers = new Random().nextInt(5) + 1;

		Group group = new Group();

		int id = ++groupCount;

		for (int i = 0; i < numberOfPlayers; i++)
		{
			group.add(UnitsGenerator.createRandomPlayer(id));
		}

		return group;
	}

	public static Player createRandomPlayer(int group)
	{
		String name = "Player " + ++playerCount + "_" + group;

		return new Player(name, new Random().nextInt(25) + 5);
	}
}
