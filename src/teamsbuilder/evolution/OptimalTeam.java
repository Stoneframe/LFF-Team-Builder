package teamsbuilder.evolution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import model.Player;
import model.Unit;

public class OptimalTeam
{
	private final Map<Predicate<Player>, Double> cache = new HashMap<>();

	private final List<Unit> units;
	private final int nbrOfTeams;

	public OptimalTeam(List<Unit> units, int nbrOfTeams)
	{
		this.units = units;
		this.nbrOfTeams = nbrOfTeams;
	}

	public double count(Predicate<Player> predicate)
	{
		return cache.computeIfAbsent(predicate, p -> countValue(p));
	}

	private double countValue(Predicate<Player> predicate)
	{
		double total = units.stream().mapToDouble(u -> u.count(predicate)).sum();

		return total / nbrOfTeams;
	}
}
