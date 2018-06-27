package teamsbuilder;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import model.Group;
import model.Team;
import model.Unit;

public class GroupSplitterLayer
	implements
		Layer
{
	private Layer subLayer;

	private TeamsValidator teamsValidator;

	public GroupSplitterLayer(Layer subLayer, TeamsValidator teamsValidator)
	{
		this.subLayer = subLayer;
		this.teamsValidator = teamsValidator;
	}

	@Override
	public List<Team> createTeams(List<Unit> units, int numberOfTeams)
	{
		List<Team> teams;

		do
		{
			teams = subLayer.createTeams(units, numberOfTeams);
		}
		while (!teamsValidator.areValid(teams) && (units = splitLargestGroup(units)) != null);

		return teams;
	}

	private List<Unit> splitLargestGroup(List<Unit> units)
	{
		System.out.println("Splitting largest group");
		System.out.println();

		List<Unit> sortedUnits = sortUnitsByNumberOfPlayersDescending(units);

		if (sortedUnits.get(0).numberOfPlayers() == 1)
		{
			return null;
		}

		Group group = (Group)sortedUnits.get(0);

		Group group1 = new Group();
		Group group2 = new Group();

		List<Unit> modifiedUnits = new LinkedList<>();

		group.split(group1, group2);

		modifiedUnits.add(group1);
		modifiedUnits.add(group2);

		for (int i = 1; i < units.size(); i++)
		{
			modifiedUnits.add(sortedUnits.get(i));
		}

		return modifiedUnits;
	}

	private List<Unit> sortUnitsByNumberOfPlayersDescending(List<Unit> units)
	{
		return units
			.stream()
			.sorted(Comparator.comparingInt((Unit u) -> u.numberOfPlayers()).reversed())
			.collect(Collectors.toList());
	}
}
