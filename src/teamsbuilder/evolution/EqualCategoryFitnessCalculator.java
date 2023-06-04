package teamsbuilder.evolution;

import java.util.LinkedList;
import java.util.List;

import model.NumberOf.Category;
import model.Team;

public class EqualCategoryFitnessCalculator
	implements
		FitnessCalculator
{
	private final List<FitnessCalculator> calculators = new LinkedList<>();

	public EqualCategoryFitnessCalculator(Category... categories)
	{
		for (Category category : categories)
		{
			calculators.add(new EqualCategoryFitnessCalculatorInternal(category));
		}
	}

	@Override
	public double calculate(List<Team> teams)
	{
		return calculators.stream()
			.mapToDouble(c -> c.calculate(teams))
			.sum();
	}

	private class EqualCategoryFitnessCalculatorInternal
		implements
			FitnessCalculator
	{
		private final Category category;

		public EqualCategoryFitnessCalculatorInternal(Category category)
		{
			this.category = category;
		}

		@Override
		public double calculate(List<Team> teams)
		{
			int max = teams.stream().mapToInt(t -> t.count(category)).max().getAsInt();
			int min = teams.stream().mapToInt(t -> t.count(category)).min().getAsInt();

			return Math.pow(max - min, 3);
			
//			return FACTOR / (FACTOR + (max - min));
		}
	}
}
