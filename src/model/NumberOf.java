package model;

import java.util.function.Predicate;

public class NumberOf
{
	public static final Category PLAYERS =
		new Category("Spelare", p -> true);

	public static final Category YOUNG_SCORE_ABLE =
		new Category("Yngre målgörare", p -> p.getAge() < 14);

	public static final Category OLDER_SCORE_ABLE =
		new Category("Äldre målgörare", p -> 50 <= p.getAge());

	public static final Category SCORE_ABLE =
		new Category("Målgörare", p -> YOUNG_SCORE_ABLE.test(p) || OLDER_SCORE_ABLE.test(p));

	public static final Category NON_SCORE_ABLE =
		new Category("Ej målgörare", p -> !SCORE_ABLE.test(p));

	public static final Category YOUNGER_CHILDREN =
		new Category("Yngre barn", p -> p.getAge() <= 6);

	public static final Category CHILDREN =
		new Category("Barn", p -> 6 < p.getAge() && p.getAge() <= 9);

	public static final Category OLDER_CHILDREN =
		new Category("Äldre barn", p -> 9 < p.getAge() && p.getAge() <= 12);

	public static final Category YOUNGER_TEENS =
		new Category("Yngre tonåringar", p -> 12 < p.getAge() && p.getAge() <= 14);

	public static final Category OLDER_TEENS =
		new Category("Äldre tonåringar", p -> 14 < p.getAge() && p.getAge() <= 18);

	public static final Category YOUNGER_ADULTS =
		new Category("Yngre vuxna", p -> 18 < p.getAge() && p.getAge() <= 30);

	public static final Category ADULTS =
		new Category("Vuxna", p -> 30 < p.getAge() && p.getAge() <= 55);

	public static final Category SENIORS =
		new Category("Seniorer", p -> 55 < p.getAge());

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
