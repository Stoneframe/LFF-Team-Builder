package registrator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import io.FileHandler;
import model.Unit;
import registrator.components.LffPanel;

public class RegistratorFrame
	extends JFrame
{
	private static final long serialVersionUID = -2805169661270719140L;

	private final LundsFFPanel lundsFFPanel;
	private final UnitListPanel unitListPanel;
	private final FormPanel formPanel;

	public RegistratorFrame()
	{
		lundsFFPanel = new LundsFFPanel();

		unitListPanel = new UnitListPanel();
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

	}

	private void windowOpened()
	{
		List<Unit> units = FileHandler.readFromFile(new File("units"));

		unitListPanel.setUnits(units);
	}

	private void windowClosed()
	{
		List<Unit> units = unitListPanel.getUnits();

		FileHandler.writeToFile(new File("units"), units);
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
