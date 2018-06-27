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
		List<Unit> sortedUnits = sortUnitsByNumberOfPlayersDescending(units);

		boolean groupIsSplit = false;

		List<Unit> modifiedUnits = new LinkedList<>();

		for (Unit unit : sortedUnits)
		{
			if (unit instanceof Group && unit.numberOfPlayers() > 1 && !((Group)unit).isLocked())
			{
				Group group = (Group)unit;

				Group group1 = new Group();
				Group group2 = new Group();

				group.split(group1, group2);

				modifiedUnits.add(group1);
				modifiedUnits.add(group2);

				groupIsSplit = true;
			}
			else
			{
				modifiedUnits.add(unit);
			}
		}

		return groupIsSplit ? modifiedUnits : null;
	}

	private List<Unit> sortUnitsByNumberOfPlayersDescending(List<Unit> units)
	{
		return units
			.stream()
			.sorted(Comparator.comparingInt((Unit u) -> u.numberOfPlayers()).reversed())
			.collect(Collectors.toList());
	}
}
