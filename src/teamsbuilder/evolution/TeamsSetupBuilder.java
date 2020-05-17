package teamsbuilder.evolution;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import model.NumberOf;
import model.Team;
import model.Unit;
import teamsbuilder.TeamSettings;

public class TeamsSetupBuilder
{
	private final List<ProgressListener> progressListeners = new LinkedList<>();

	private final List<Unit> units;
	private final TeamSettings settings;

	private final List<TeamsSetup> setups = new LinkedList<>();

	public TeamsSetupBuilder(List<Unit> units, TeamSettings settings)
	{
		this.units = units;
		this.settings = settings;
	}

	public List<Team> createTeams()
	{
		initalizeRandomSetup();

		for (int i = 0; i < 100; i++)
		{
			notifyProgress(i);

			sortByFitness();
			cullTheWeak();
			reproduce();
		}

		notifyProgress(100);

		return getTeams();
	}

	public void addProgressListener(ProgressListener listener)
	{
		progressListeners.add(listener);
	}

	private void notifyProgress(int percent)
	{
		for (ProgressListener listener : progressListeners)
		{
			listener.progressChanged(percent);
		}
	}

	private boolean initalizeRandomSetup()
	{
		return setups.add(createInitialTeamsSetup());
	}

	private TeamsSetup createInitialTeamsSetup()
	{
		List<Team> teams = createRandomTeams();

		return new TeamsSetup(
			teams,
			getFitnessCalculator(),
			settings);
	}

	private FitnessCalculator getFitnessCalculator()
	{
		return new LffFitnessCalculator(
			createOptimalTeam(),
			NumberOf.PLAYERS,
			NumberOf.SCORE_ABLE,
			NumberOf.TEEN_AGERS);
	}

	private List<Team> createRandomTeams()
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
			teams.add(new Team(settings.getTeamNames().get(i)));
		}

		return teams;
	}

	private void sortByFitness()
	{
		setups.sort(byHighestFitness());

		System.out.println(setups.get(0));
	}

	private Comparator<? super TeamsSetup> byHighestFitness()
	{
		return (s1, s2) -> Double.compare(s2.getFitness(), s1.getFitness());
	}

	private void cullTheWeak()
	{
		int size = setups.size();

		for (int i = 0; i < size - 5; i++)
		{
			setups.remove(5);
		}
	}

	private void reproduce()
	{
		int size = setups.size();

		for (int i = 0; i < size; i++)
		{
			setups.addAll(setups.get(i).reproduce());
		}
	}

	private List<Team> getTeams()
	{
		return setups.get(0).getTeams();
	}

	private OptimalTeam createOptimalTeam()
	{
		return new OptimalTeam(units, settings.getNumberOfTeams());
	}
}
