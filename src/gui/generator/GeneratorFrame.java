package gui.generator;

import java.awt.BorderLayout;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import gui.LffFrameBase;
import gui.components.LffPanel;
import io.FileHandler;
import logging.LoggerFactory;
import model.NumberOf;
import model.NumberOf.Category;
import model.Team;
import teamsbuilder.TeamSettings;
import teamsbuilder.evolution.EqualCategoryFitnessCalculator;
import teamsbuilder.evolution.TeamsSetupBuilder;

public class GeneratorFrame
	extends LffFrameBase
{
	private static final long serialVersionUID = 1932555429400080599L;

	private static final Category[] CATEGORIES = new Category[]
	{
		NumberOf.PLAYERS,
		NumberOf.SCORE_ABLE,
		NumberOf.YOUNG_SCORE_ABLE,
		NumberOf.OLDER_SCORE_ABLE,
		NumberOf.NON_SCORE_ABLE,
		NumberOf.YOUNGER_CHILDREN,
		NumberOf.CHILDREN,
		NumberOf.OLDER_CHILDREN,
		NumberOf.YOUNGER_TEENS,
		NumberOf.OLDER_TEENS,
		NumberOf.YOUNGER_ADULTS,
		NumberOf.ADULTS,
		NumberOf.SENIORS,
		NumberOf.PEAK,
	};

	protected static final String TEAM_FOLDER = "Lag";
	protected static final String TEAM_FILE = "Lag.txt";

	private final Logger logger = LoggerFactory.createLogger(GeneratorFrame.class.getName());

	private final SettingsPanel settingsPanel;
	private final TeamListPanel teamListPanel;

	public GeneratorFrame()
	{
		super("Generering");

		logger.info("Starting Generator");

		unitListModel.addListDataListener(new ListDataListener()
		{
			@Override
			public void intervalRemoved(ListDataEvent e)
			{
				updateNumberOfPlayers();
			}

			@Override
			public void intervalAdded(ListDataEvent e)
			{
				updateNumberOfPlayers();
			}

			@Override
			public void contentsChanged(ListDataEvent e)
			{
				updateNumberOfPlayers();
			}

			private void updateNumberOfPlayers()
			{
				settingsPanel.setNbrOfPlayers(unitListPanel.getNbrOfPlayers());
			}
		});

		settingsPanel = new SettingsPanel();
		settingsPanel.addGenerateButtonActionListener(l -> onGenerate());

		teamListPanel = new TeamListPanel();
		teamListPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

		LffPanel centerPanel = new LffPanel(new BorderLayout());

		centerPanel.add(settingsPanel, BorderLayout.WEST);
		centerPanel.add(teamListPanel, BorderLayout.CENTER);

		add(centerPanel, BorderLayout.CENTER);

		pack();
	}

	@Override
	protected void onWindowOpened()
	{
		settingsPanel.setNbrOfPlayers(unitListPanel.getNbrOfPlayers());
	}

	@Override
	protected void onWindowClosed()
	{
	}

	@Override
	protected void loadPlayers()
	{
		unitListModel.loadFromFolder(PLAYER_FOLDER);
	}

	@Override
	protected void savePlayers()
	{
	}

	@Override
	protected void OnMockPlayers()
	{
		super.OnMockPlayers();

		settingsPanel.setNbrOfPlayers(unitListPanel.getNbrOfPlayers());
	}

	private void onGenerate()
	{
		TeamSettings settings = settingsPanel.getTeamSettings();

		new TeamsBuilderWorker(settings).execute();
	}

	private void printTeamsToFile(List<Team> teams)
	{
		Path filePath = Paths.get(TEAM_FOLDER, TEAM_FILE);

		logger.info("Writing teams to file: " + filePath.toAbsolutePath());

		FileHandler.printTeams(filePath, teams);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new GeneratorFrame();
			}
		});
	}

	private class TeamsBuilderWorker
		extends SwingWorker<List<Team>, Object>
	{
		private ProgressFrame progressFrame;
		private TeamsSetupBuilder teamsBuilder;

		public TeamsBuilderWorker(TeamSettings settings)
		{
			progressFrame = new ProgressFrame();
			progressFrame.setLocationRelativeTo(GeneratorFrame.this);

			teamsBuilder = new TeamsSetupBuilder(
				unitListPanel.getUnits(),
				settings,
				CATEGORIES,
				new EqualCategoryFitnessCalculator(CATEGORIES));

			teamsBuilder.addProgressListener(progressFrame);
		}

		@Override
		protected List<Team> doInBackground() throws Exception
		{
			settingsPanel.setEnabled(false);
			progressFrame.setVisible(true);

			return teamsBuilder.createTeams();
		}

		@Override
		protected void done()
		{
			try
			{
				teamListPanel.showTeams(get());
				progressFrame.setDetails(getDetails());

				printTeamsToFile(get());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			settingsPanel.setEnabled(true);
		}

		private String getDetails() throws Exception
		{
			StringBuilder builder = new StringBuilder();

			builder.append(header());

			for (Category category : CATEGORIES)
			{
				builder.append(detailsAbout(category));
			}

			return builder.toString();
		}

		private String header()
		{
			return "Kategori (per lag):         Min:    Max:    Tot:" + System.lineSeparator();
		}

		private String detailsAbout(Category category) throws Exception
		{
			return String
				.format(
					"%-20s        %4d    %4d    %4d" + System.lineSeparator(),
					category,
					lowestNumberOf(category),
					highestNumberOf(category),
					totalNumberOf(category));
		}

		private int lowestNumberOf(Category category) throws Exception
		{
			return get().stream()
				.mapToInt(t -> t.count(category))
				.min()
				.getAsInt();
		}

		private int highestNumberOf(Category category) throws Exception
		{
			return get().stream()
				.mapToInt(t -> t.count(category))
				.max()
				.getAsInt();
		}

		private int totalNumberOf(Category category)
		{
			return (int)unitListPanel.getUnits()
				.stream()
				.flatMap(u -> u.getPlayers().stream())
				.filter(p -> category.test(p))
				.count();
		}
	}
}
