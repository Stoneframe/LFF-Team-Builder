package gui.generator;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import gui.LundsFFPanel;
import gui.UnitListPanel;
import gui.components.LffPanel;
import io.FileHandler;
import logging.LoggerFactory;
import model.Team;
import model.Unit;
import teamsbuilder.TeamsBuilder;
import util.UnitsUtil;

public class GeneratorFrame
	extends JFrame
{
	private static final long serialVersionUID = 1932555429400080599L;

	private final Logger logger = LoggerFactory.createLogger(GeneratorFrame.class.getName());

	private final LundsFFPanel lundsFFPanel;
	private final UnitListPanel unitListPanel;
	private final SettingsPanel settingsPanel;
	private final TeamListPanel teamListPanel;

	public GeneratorFrame()
	{
		logger.info("Starting Generator");

		lundsFFPanel = new LundsFFPanel();

		unitListPanel = new UnitListPanel("Spelare");
		unitListPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		unitListPanel.setRemoveButtonVisible(false);

		settingsPanel = new SettingsPanel();
		settingsPanel.addGenerateButtonActionListener(l -> onGenerate());

		teamListPanel = new TeamListPanel();
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

		KeyStroke mock = KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK, false);

		unitListPanel.registerKeyboardAction(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				List<Unit> units = UnitsUtil.createRandomUnitList(true);

				units.forEach(u -> unitListPanel.addUnit(u));

				settingsPanel.setNbrOfPlayers(unitListPanel.getNbrOfPlayers());
			}
		}, "Mock", mock, JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	private void windowOpened()
	{
		List<Unit> allUnits = FileHandler.readFromDirectory(new File("Spelare"));

		unitListPanel.setUnits(allUnits);

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
