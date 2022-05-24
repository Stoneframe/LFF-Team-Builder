package gui.registrator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import gui.LffFrameBase;
import gui.components.LffPanel;
import logging.LoggerFactory;

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
	}

	@Override
	protected void onWindowClosed()
	{
	}

	@Override
	protected void loadPlayers()
	{
		unitListModel.loadFromFile(PLAYER_FOLDER);
	}

	@Override
	protected void savePlayers()
	{
		unitListModel.save(PLAYER_FOLDER);
	}

	private void onAddUnit()
	{
		unitListPanel.addUnit(unitFormPanel.getUnit());
		unitFormPanel.reset();
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
