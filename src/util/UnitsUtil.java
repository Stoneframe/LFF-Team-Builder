package util;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import model.Group;
import model.Player;
import model.Team;
import model.Unit;

public class UnitsUtil
{
	private static int groupCount = 0;
	private static int playerCount = 0;

	public static List<Unit> createRandomUnitList()
	{
		return createRandomUnitList(false);
	}

	public static List<Unit> createRandomUnitList(boolean includeLockedGroups)
	{
		List<Unit> units = new LinkedList<>();

		int numberOfUnits = new Random().nextInt(20) + 10;

		for (int i = 0; i < numberOfUnits; i++)
		{
			units.add(UnitsUtil.createRandomUnit(includeLockedGroups));
		}

		return units;
	}

	public static Unit createRandomUnit(boolean includeLockedGroups)
	{
		if (new Random().nextBoolean())
		{
			return UnitsUtil.createRandomPlayer(0);
		}
		else
		{
			return UnitsUtil.createRandomGroup(includeLockedGroups);
		}
	}

	public static Group createRandomGroup(boolean includeLockedGroups)
	{
		Random random = new Random();

		int numberOfPlayers = random.nextInt(4) + 2;

		Group group = new Group(includeLockedGroups && random.nextInt(4) == 1);

		int id = ++groupCount;

		for (int i = 0; i < numberOfPlayers; i++)
		{
			group.add(UnitsUtil.createRandomPlayer(id));
		}

		return group;
	}

	public static Player createRandomPlayer(int group)
	{
		String name = "Player " + ++playerCount + "_" + group;

		return new Player(name, new Random().nextInt(25) + 5);
	}

	public static void print(List<Unit> units, List<Team> teams)
	{
		System.out.println("Units:");
		System.out.println();
		units.forEach(u -> System.out.println(u + System.lineSeparator()));

		System.out.println();
		System.out.println();

		System.out.println("Teams:");
		System.out.println();
		teams.forEach(t -> System.out.println(t));
	}
}
