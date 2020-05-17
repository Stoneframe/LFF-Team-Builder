package teamsbuilder.evolution;

import java.util.Arrays;
import java.util.List;

import model.Team;

public class TotalFitnessCalculator
	implements
		FitnessCalculator
{
	private final FitnessCalculator[] calculators;

	public TotalFitnessCalculator(FitnessCalculator... calculators)
	{
		this.calculators = calculators;
	}

	@Override
	public double calculate(List<Team> teams)
	{
		return Arrays.stream(calculators)
			.mapToDouble(c -> c.calculate(teams))
			.average()
			.getAsDouble();
	}
}
