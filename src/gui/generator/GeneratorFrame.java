package gui.generator;

import java.awt.BorderLayout;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import gui.LffFrameBase;
import gui.components.LffPanel;
import io.FileHandler;
import logging.LoggerFactory;
import model.NumberOf;
import model.NumberOf.Category;
import model.Team;
import model.Unit;
import teamsbuilder.TeamSettings;
import teamsbuilder.evolution.TeamsSetupBuilder;

public class GeneratorFrame
	extends LffFrameBase
{
	private static final long serialVersionUID = 1932555429400080599L;

	protected static final String TEAM_FOLDER = "Lag";
	protected static final String TEAM_FILE = "Lag.txt";

	private final Logger logger = LoggerFactory.createLogger(GeneratorFrame.class.getName());

	private final SettingsPanel settingsPanel;
	private final TeamListPanel teamListPanel;

	public GeneratorFrame()
	{
		super("Generering");

		logger.info("Starting Generator");

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
		Path folderPath = Paths.get(PLAYER_FOLDER);
		logger.info("Reading from folder: " + folderPath.toAbsolutePath());

		List<Unit> allUnits = FileHandler.readFromDirectory(folderPath);
		logger.info("Read " + allUnits.size() + " units");

		unitListPanel.setUnits(allUnits);
		settingsPanel.setNbrOfPlayers(unitListPanel.getNbrOfPlayers());
	}

	@Override
	protected void onWindowClosed()
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

			teamsBuilder = new TeamsSetupBuilder(unitListPanel.getUnits(), settings);
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
			builder.append(detailsAbout(NumberOf.PLAYERS));
			builder.append(detailsAbout(NumberOf.SCORE_ABLE));
			builder.append(detailsAbout(NumberOf.YOUNG_CHILDREN));
			builder.append(detailsAbout(NumberOf.CHILDREN));
			builder.append(detailsAbout(NumberOf.TEEN_AGERS));
			builder.append(detailsAbout(NumberOf.YOUNG_ADULTS));
			builder.append(detailsAbout(NumberOf.ADULTS));
			builder.append(detailsAbout(NumberOf.SENIORS));

			return builder.toString();
		}

		private String header()
		{
			return "Kategori (per lag):         Min:    Max:" + System.lineSeparator();
		}

		private String detailsAbout(Category category) throws Exception
		{
			return String
				.format(
					"%-20s        %4d    %4d" + System.lineSeparator(),
					category,
					lowestNumberOf(category),
					highestNumberOf(category));
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
	}
}
