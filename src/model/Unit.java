package model;

import java.util.List;
import java.util.function.Predicate;

public interface Unit
{
	public int count(Predicate<Player> predicate);

	public List<Player> getPlayers();
}
