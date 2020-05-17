package model;

import java.util.function.Predicate;

public class NumberOf
{
	public static final Predicate<Player> SCORE_ABLE = p -> p.getAge() < 13 || 50 <= p.getAge();

	public static final Predicate<Player> TEEN_AGERS = p -> 12 < p.getAge() && p.getAge() < 20;

	public static final Predicate<Player> PLAYERS = p -> true;
}
