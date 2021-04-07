package gui.registrator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import gui.LffFrameBase;
import gui.components.LffPanel;
import io.FileHandler;
import logging.LoggerFactory;
import model.Unit;

public class RegistratorFrame
	extends LffFrameBase
{
	private static final long serialVersionUID = -2805169661270719140L;

	private final Logger logger = LoggerFactory.createLogger(RegistratorFrame.class.getName());

	private final FormPanel unitFormPanel;

	public RegistratorFrame()
	{
		super("Registrering");

		logger.info("Starting Registrator");

		unitFormPanel = new FormPanel(FormPanel.ADD_MODE);
		unitFormPanel.addOkButtonActionListener(l -> onAddUnit());

		LffPanel centerPanel = new LffPanel(new FlowLayout(FlowLayout.LEFT));

		centerPanel.add(unitFormPanel);

		add(centerPanel, BorderLayout.CENTER);

		pack();

		unitFormPanel.requestFocus();
	}

	@Override
	protected void onWindowOpened()
	{
		readUnitsFromFile();
	}

	@Override
	protected void onWindowClosed()
	{
		writeUnitsToFile();
	}

	private void onAddUnit()
	{
		unitListPanel.addUnit(unitFormPanel.getUnit());
		unitFormPanel.reset();
	}

	private void readUnitsFromFile()
	{
		String fileName = FileHandler.getFileName();

		Path filePath = Paths.get(PLAYER_FOLDER, fileName);
		logger.info("Reading from file: " + filePath.toAbsolutePath());

		List<Unit> units = FileHandler.readFromFile(filePath);

		unitListPanel.setUnits(units);
	}

	private void writeUnitsToFile()
	{
		String fileName = FileHandler.getFileName();

		Path filePath = Paths.get(PLAYER_FOLDER, fileName);
		logger.info("Writing to file: " + filePath.toAbsolutePath());

		List<Unit> units = unitListPanel.getUnits();
		logger.info("Write " + units.size() + " units");

		FileHandler.writeToFile(filePath, units);
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
