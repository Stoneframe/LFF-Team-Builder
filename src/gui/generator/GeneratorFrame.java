package gui.generator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gui.LundsFFPanel;
import gui.UnitListPanel;
import gui.components.LffPanel;
import io.FileHandler;
import model.Team;
import model.Unit;
import teamsbuilder.TeamsBuilder;

public class GeneratorFrame
	extends JFrame
{
	private static final long serialVersionUID = 1932555429400080599L;

	private final LundsFFPanel lundsFFPanel;
	private final UnitListPanel unitListPanel;
	private final SettingsPanel settingsPanel;
	private final TeamListPanel teamListPanel;

	public GeneratorFrame()
	{
		lundsFFPanel = new LundsFFPanel();

		unitListPanel = new UnitListPanel("Spelare");
		unitListPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		unitListPanel.setRemoveButtonVisible(false);

		settingsPanel = new SettingsPanel();
		settingsPanel.addGenerateButtonActionListener(l -> onGenerate());

		teamListPanel = new TeamListPanel();
		teamListPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		teamListPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

		LffPanel centerPanel = new LffPanel(new BorderLayout());

		centerPanel.add(settingsPanel, BorderLayout.WEST);
		centerPanel.add(teamListPanel, BorderLayout.CENTER);

		setTitle("Generering");

		setLayout(new BorderLayout());

		add(lundsFFPanel, BorderLayout.NORTH);
		add(unitListPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);

		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		addWindowListener(new WindowAdapter()
		{
			public void windowOpened(WindowEvent e)
			{
				GeneratorFrame.this.windowOpened();
			};
		});
	}

	private void windowOpened()
	{
		List<Unit> allUnits = FileHandler.readFromDirectory();

		unitListPanel.setUnits(allUnits);

		settingsPanel.setNbrOfPlayers(unitListPanel.getNbrOfPlayers());
	}

	private void onGenerate()
	{
		TeamsBuilder builder = new TeamsBuilder(
				age -> age < 15 && 50 < age,
				settingsPanel.getTeamNames());

		builder.setSplitNonLockedGroups(true);

		List<Team> teams = builder.createTeams(
			unitListPanel.getUnits(),
			settingsPanel.getNbrOfTeams());

		teamListPanel.showTeams(teams);

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
