package teamsbuilder;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import model.Group;
import model.Group.GroupSplit;
import model.Team;
import model.Unit;

public class GroupSplitterLayer
	implements
		Layer
{
	private Layer subLayer;
	private Function<Integer, Boolean> scoringRule;
	private TeamsValidator teamsValidator;

	public GroupSplitterLayer(
			Layer subLayer,
			Function<Integer, Boolean> scoringRule,
			TeamsValidator teamsValidator)
	{
		this.subLayer = subLayer;
		this.scoringRule = scoringRule;
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
		List<Unit> sortedUnits = sortUnitsByNumberOfScoreablePlayersDescending(units);

		boolean groupIsSplit = false;

		List<Unit> modifiedUnits = new LinkedList<>();

		for (Unit unit : sortedUnits)
		{
			if (!groupIsSplit
					&& unit instanceof Group
					&& unit.numberOfPlayers() > 1
					&& !((Group)unit).isLocked())
			{
				Group group = (Group)unit;

				GroupSplit groupSplit = group.split();

				modifiedUnits.add(groupSplit.getUnit1());
				modifiedUnits.add(groupSplit.getUnit2());

				groupIsSplit = true;
			}
			else
			{
				modifiedUnits.add(unit);
			}
		}

		return groupIsSplit ? modifiedUnits : null;
	}

	// private List<Unit> sortUnitsByNumberOfPlayersDescending(List<Unit> units)
	// {
	// return units
	// .stream()
	// .sorted(Comparator.comparingInt((Unit u) ->
	// u.numberOfPlayers()).reversed())
	// .collect(Collectors.toList());
	// }

	private List<Unit> sortUnitsByNumberOfScoreablePlayersDescending(List<Unit> units)
	{
		return units
			.stream()
			.sorted(
				Comparator
					.comparingInt((Unit u) -> u.numberOfScoreablePlayers(scoringRule))
					.reversed())
			.collect(Collectors.toList());
	}
}
