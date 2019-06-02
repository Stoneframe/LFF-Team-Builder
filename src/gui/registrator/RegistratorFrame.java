package gui.registrator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import model.Unit;
import util.UnitsUtil;

public class RegistratorFrame
	extends JFrame
{
	private static final long serialVersionUID = -2805169661270719140L;

	private final Logger logger = LoggerFactory.createLogger(this);

	private final LundsFFPanel lundsFFPanel;
	private final UnitListPanel unitListPanel;
	private final FormPanel formPanel;

	public RegistratorFrame()
	{
		logger.info("Starting Registrator");

		lundsFFPanel = new LundsFFPanel();

		unitListPanel = new UnitListPanel("Spelare");
		unitListPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		unitListPanel.addRemoveButtonActionListener(l -> removeUnit());

		formPanel = new FormPanel();
		formPanel.addAddButtonActionListener(l -> addUnit());

		LffPanel centerPanel = new LffPanel(new FlowLayout(FlowLayout.LEFT));

		centerPanel.add(formPanel);

		setTitle("Registrering");

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
				RegistratorFrame.this.windowOpened();
			};

			public void windowClosing(WindowEvent e)
			{
				RegistratorFrame.this.windowClosed();
			};
		});

		KeyStroke mock = KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK, false);

		unitListPanel.registerKeyboardAction(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				List<Unit> units = UnitsUtil.createRandomUnitList(true);

				units.forEach(u -> unitListPanel.addUnit(u));
			}
		}, "Mock", mock, JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	private void windowOpened()
	{
		String fileName = FileHandler.getFileName();

		List<Unit> units = FileHandler.readFromFile(new File(fileName));

		unitListPanel.setUnits(units);
	}

	private void windowClosed()
	{
		String fileName = FileHandler.getFileName();

		List<Unit> units = unitListPanel.getUnits();

		FileHandler.writeToFile(new File(fileName), units);
	}

	private void addUnit()
	{
		unitListPanel.addUnit(formPanel.getUnit());
		formPanel.reset();
	}

	private void removeUnit()
	{
		unitListPanel.removeUnit(unitListPanel.getSelectedUnit());
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
