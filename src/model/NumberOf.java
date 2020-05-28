package model;

import java.util.function.Predicate;

public class NumberOf
{
	public static final Category PLAYERS =
			new Category("Spelare", p -> true);

	public static final Category SCORE_ABLE =
			new Category("Målgörare", p -> p.getAge() < 13 || 50 <= p.getAge());

	public static final Category NON_SCORE_ABLE =
			new Category("Ej målgörare", p -> !SCORE_ABLE.test(p));

	public static final Category YOUNG_CHILDREN =
			new Category("Små barn", p -> p.getAge() <= 7);

	public static final Category CHILDREN =
			new Category("Barn", p -> 7 < p.getAge() && p.getAge() <= 12);

	public static final Category TEEN_AGERS =
			new Category("Tonåringar", p -> 12 < p.getAge() && p.getAge() <= 19);

	public static final Category YOUNG_ADULTS =
			new Category("Unga vuxna", p -> 19 < p.getAge() && p.getAge() <= 29);

	public static final Category ADULTS =
			new Category("Vuxna", p -> 29 < p.getAge() && p.getAge() <= 49);

	public static final Category SENIORS =
			new Category("Seniorer", p -> 49 < p.getAge());

	public static class Category
		implements
			Predicate<Player>
	{
		private final String title;
		private final Predicate<Player> predicate;

		public Category(String title, Predicate<Player> predicate)
		{
			this.title = title;
			this.predicate = predicate;
		}

		@Override
		public boolean test(Player t)
		{
			return predicate.test(t);
		}

		@Override
		public String toString()
		{
			return title;
		}
	}
}
