package gui.registrator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import gui.LffFrameBase;
import gui.components.LffPanel;
import io.FileHandler;
import logging.LoggerFactory;
import model.Group;
import model.Player;
import model.Unit;

public class RegistratorFrame
	extends LffFrameBase
{
	private static final long serialVersionUID = -2805169661270719140L;

	private final Logger logger = LoggerFactory.createLogger(RegistratorFrame.class.getName());

	private final FormPanel formPanel;

	public RegistratorFrame()
	{
		super("Registrering");

		logger.info("Starting Registrator");

		unitListPanel.setRemoveButtonVisible(true);
		unitListPanel.addEditButtonActionListener(l -> onEditUnit());
		unitListPanel.addSplitButtonActionListener(l -> onSplitUnit());
		unitListPanel.addMergeButtonActionListener(l -> onMergeUnits());
		unitListPanel.addRemoveButtonActionListener(l -> onRemoveUnit());

		formPanel = new FormPanel(FormPanel.ADD_MODE);
		formPanel.addOkButtonActionListener(l -> onAddUnit());

		LffPanel centerPanel = new LffPanel(new FlowLayout(FlowLayout.LEFT));

		centerPanel.add(formPanel);

		add(centerPanel, BorderLayout.CENTER);

		pack();

		formPanel.requestFocus();
	}

	@Override
	protected void onWindowOpened()
	{
		String fileName = FileHandler.getFileName();

		Path filePath = Paths.get(PLAYER_FOLDER, fileName);
		logger.info("Reading from file: " + filePath.toAbsolutePath());

		List<Unit> units = FileHandler.readFromFile(filePath);

		unitListPanel.setUnits(units);
	}

	@Override
	protected void onWindowClosed()
	{
		String fileName = FileHandler.getFileName();

		Path filePath = Paths.get(PLAYER_FOLDER, fileName);
		logger.info("Writing to file: " + filePath.toAbsolutePath());

		List<Unit> units = unitListPanel.getUnits();
		logger.info("Read " + units.size() + " units");

		FileHandler.writeToFile(filePath, units);
	}

	private void onAddUnit()
	{
		unitListPanel.addUnit(formPanel.getUnit());
		formPanel.reset();
	}

	private void onEditUnit()
	{
		Unit selectedUnit = unitListPanel.getSelectedUnit();

		JFrame frame = new JFrame("Redigera");

		FormPanel formPanel = new FormPanel(FormPanel.SAVE_MODE);
		formPanel.setUnit(selectedUnit);
		formPanel.addOkButtonActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				unitListPanel.replaceUnit(selectedUnit, formPanel.getUnit());
				frame.dispose();
			}
		});

		LffPanel centerPanel = new LffPanel(new FlowLayout(FlowLayout.LEFT));
		centerPanel.add(formPanel);

		frame.setLayout(new BorderLayout());
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.setSize(600, 520);
		frame.setLocationRelativeTo(this);
		frame.setVisible(true);
	}

	private void onSplitUnit()
	{
		JFrame frame = new JFrame("Dela");

		Unit selectedUnit = unitListPanel.getSelectedUnit();

		SplitPanel splitPanel = new SplitPanel(selectedUnit);
		splitPanel.addOkButtonActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Unit unit1 = splitPanel.getUnit1();
				Unit unit2 = splitPanel.getUnit2();

				unitListPanel.replaceUnit(selectedUnit, unit1, unit2);

				frame.dispose();
			}
		});

		frame.setLayout(new BorderLayout());
		frame.add(splitPanel, BorderLayout.CENTER);
		frame.setLocationRelativeTo(this);
		frame.pack();
		frame.setVisible(true);
	}

	private void onMergeUnits()
	{
		List<Unit> units = unitListPanel.getSelectedUnits();

		List<Player> allPlayers = units.stream()
			.flatMap(u -> u.getPlayers().stream())
			.collect(Collectors.toList());

		Group group = new Group(getIsLocked(), allPlayers);

		replace(units, group);
	}

	private boolean getIsLocked()
	{
		int choice = JOptionPane.showConfirmDialog(
			this,
			"Vill du L�sa den nya gruppen?",
			"L�sa grupp?",
			JOptionPane.YES_NO_OPTION);

		return choice == JOptionPane.YES_OPTION;
	}

	private void replace(List<Unit> units, Group group)
	{
		unitListPanel.replaceUnit(units.get(0), group);

		for (int i = 1; i < units.size(); i++)
		{
			unitListPanel.removeUnit(units.get(i));
		}
	}

	private void onRemoveUnit()
	{
		for (Unit unit : unitListPanel.getSelectedUnits())
		{
			unitListPanel.removeUnit(unit);
		}
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new RegistratorFrame();
			}
		});
	}
}
