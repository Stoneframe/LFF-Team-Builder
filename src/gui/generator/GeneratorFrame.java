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
import model.Team;
import model.Unit;
import teamsbuilder.TeamSettings;
import teamsbuilder.evolution.ProgressListener;
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

		unitListPanel.setEditButtonVisible(false);
		unitListPanel.setSplitButtonVisible(false);
		unitListPanel.setMergeButtonVisible(false);
		unitListPanel.setRemoveButtonVisible(false);

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
		private final TeamSettings settings;

		public TeamsBuilderWorker(TeamSettings settings)
		{
			this.settings = settings;
		}

		@Override
		protected List<Team> doInBackground() throws Exception
		{
			settingsPanel.setEnabled(false);

			TeamsSetupBuilder builder = new TeamsSetupBuilder(unitListPanel.getUnits(), settings);

			builder.addProgressListener(new ProgressListener()
			{
				@Override
				public void progressChanged(int percent)
				{
					settingsPanel.setProgress(percent);
				}
			});

			return builder.createTeams();
		}

		@Override
		protected void done()
		{
			try
			{
				teamListPanel.showTeams(get());

				printTeamsToFile(get());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			settingsPanel.setEnabled(true);
		}
	}
}
