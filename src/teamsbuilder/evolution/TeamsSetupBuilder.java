package teamsbuilder.evolution;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import model.NumberOf.Category;
import model.Team;
import model.Unit;
import teamsbuilder.TeamSettings;

public class TeamsSetupBuilder
{
	private static final int NUMBER_OF_ITERATIONS = 20;
	private static final int NUMBER_TO_KEEP = 500;
	private static final int NUMBER_OF_CHILDREN = 1;

	private final List<ProgressListener> progressListeners = new LinkedList<>();

	private final List<Unit> units;
	private final TeamSettings settings;

	private final Category[] categories;

	private final List<TeamsSetup> setups = new LinkedList<>();

	private final FitnessCalculator fitnessCalculator;

	private int progress = 0;

	public TeamsSetupBuilder(
		List<Unit> units,
		TeamSettings settings,
		Category[] categories,
		FitnessCalculator fitnessCalculator)
	{
		this.units = units;
		this.settings = settings;
		this.categories = categories;

		this.fitnessCalculator = fitnessCalculator;
	}

	public List<Team> createTeams()
	{
		initalizeRandomSetup();

		double bestFitness = 100;

		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++)
		{
			// notifyProgress(i * 100 / NUMBER_OF_ITERATIONS);

			sortByFitness();
			cullTheWeak();
			reproduce();

			double curr = setups.get(0).getFitness();

			if (curr < bestFitness)
			{
				bestFitness = curr;

				System.out.println(i * 100 / NUMBER_OF_ITERATIONS + ": " + bestFitness);
			}
		}

		notifyProgress(100);

		System.out.println(setups.get(0).getFitness());

		return getTeams();
	}

	public void addProgressListener(ProgressListener listener)
	{
		progressListeners.add(listener);
	}

	private void notifyProgress()
	{
		notifyProgress(progress++ * 100 / (NUMBER_OF_ITERATIONS * NUMBER_TO_KEEP));
	}

	private void notifyProgress(int percent)
	{
		for (ProgressListener listener : progressListeners)
		{
			listener.progressChanged(percent);
		}
	}

	private void initalizeRandomSetup()
	{
		for (int i = 0; i < NUMBER_TO_KEEP; i++)
		{
			setups.add(createRandomTeamsSetup());
		}
	}

	private TeamsSetup createRandomTeamsSetup()
	{
		List<Unit> shuffledUnits = new LinkedList<>(units);

		Collections.shuffle(shuffledUnits);

		List<Team> teams = createRandomTeams(shuffledUnits);

		return new TeamsSetup(fitnessCalculator, categories, teams);
	}

	private List<Team> createRandomTeams(List<Unit> units)
	{
		List<Team> teams = createEmptyTeams();

		for (int i = 0; i < units.size(); i++)
		{
			Team team = teams.get(i % teams.size());

			team.add(units.get(i));
		}

		return teams;
	}

	private List<Team> createEmptyTeams()
	{
		List<Team> teams = new LinkedList<>();

		for (int i = 0; i < settings.getNumberOfTeams(); i++)
		{
			teams.add(new Team(getTeamName(i)));
		}

		return teams;
	}

	private String getTeamName(int index)
	{
		if (index < settings.getTeamNames().size())
		{
			return settings.getTeamNames().get(index);
		}
		else
		{
			return "Team " + (index + 1);
		}
	}

	private void sortByFitness()
	{
		setups.sort(byBestFitness());
	}

	private Comparator<? super TeamsSetup> byBestFitness()
	{
		return Comparator.comparing(Util.cache(s -> s.getFitness()));
	}

	private void cullTheWeak()
	{
		int size = setups.size();

		for (int i = 0; i < size - NUMBER_TO_KEEP; i++)
		{
			setups.remove(NUMBER_TO_KEEP);
		}
	}

	private void reproduce()
	{
		for (int i = 0; i < NUMBER_TO_KEEP; i++)
		{
			setups.addAll(setups.get(i).reproduce(NUMBER_OF_CHILDREN * Math.max(1, 20 - i)));
			notifyProgress();
		}
	}

	private List<Team> getTeams()
	{
		return setups.get(0).getTeams();
	}
}
