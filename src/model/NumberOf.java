package model;

import java.util.function.Predicate;

public class NumberOf
{
	public static final Counter SCORE_ABLE = new Counter(p -> p.getAge() < 13 || 50 <= p.getAge());
	public static final Counter TEEN_AGERS = new Counter(p -> 12 < p.getAge() && p.getAge() < 20);
	public static final Counter PLAYERS = new Counter(p -> true);

	public static class Counter
		implements
			Predicate<Player>
	{
		private final Predicate<Player> predicate;

		public Counter(Predicate<Player> predicate)
		{
			this.predicate = predicate;
		}

		@Override
		public boolean test(Player t)
		{
			return predicate.test(t);
		}
	}
}
