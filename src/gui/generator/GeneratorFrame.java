package gui.generator;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

import gui.LffFrameBase;
import gui.components.LffPanel;
import io.FileHandler;
import logging.LoggerFactory;
import model.Team;
import model.Unit;
import teamsbuilder.TeamsBuilder;

public class GeneratorFrame
	extends LffFrameBase
{
	private static final long serialVersionUID = 1932555429400080599L;

	private final Logger logger = LoggerFactory.createLogger(GeneratorFrame.class.getName());

	private final SettingsPanel settingsPanel;
	private final TeamListPanel teamListPanel;

	public GeneratorFrame()
	{
		super("Generering");

		logger.info("Starting Generator");

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
		List<Unit> allUnits = FileHandler.readFromDirectory(new File("Spelare"));

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
		TeamsBuilder builder = settingsPanel.getTeamsBuilder();

		List<Team> teams = builder.createTeams(
			unitListPanel.getUnits(),
			settingsPanel.getNbrOfTeams());

		teamListPanel.showTeams(teams, builder.getScoringRule());

		FileHandler.printTeams(new File("Lag\\lag.txt"), teams);
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
}
