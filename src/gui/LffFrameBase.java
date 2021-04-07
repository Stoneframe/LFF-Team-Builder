package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import gui.components.LffPanel;
import gui.registrator.FormPanel;
import gui.registrator.SplitPanel;
import model.Group;
import model.Player;
import model.Unit;
import util.UnitsUtil;

public abstract class LffFrameBase
	extends JFrame
{
	private static final long serialVersionUID = 4706616327895328641L;

	protected static final String PLAYER_FOLDER = "Spelare";

	protected final LundsFFPanel lundsFFPanel;
	protected final UnitListPanel unitListPanel;

	protected LffFrameBase(String title)
	{
		lundsFFPanel = new LundsFFPanel();

		unitListPanel = new UnitListPanel("Spelare");
		unitListPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		unitListPanel.setRemoveButtonVisible(true);
		unitListPanel.addEditButtonActionListener(l -> onEditUnit());
		unitListPanel.addSplitButtonActionListener(l -> onSplitUnit());
		unitListPanel.addMergeButtonActionListener(l -> onMergeUnits());
		unitListPanel.addRemoveButtonActionListener(l -> onRemoveUnit());

		setTitle(title);

		setLayout(new BorderLayout());

		add(lundsFFPanel, BorderLayout.NORTH);
		add(unitListPanel, BorderLayout.WEST);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		addWindowListener(new WindowAdapter()
		{
			public void windowOpened(WindowEvent e)
			{
				LffFrameBase.this.onWindowOpened();
			};

			public void windowClosing(WindowEvent e)
			{
				LffFrameBase.this.onWindowClosed();
			};
		});

		KeyStroke mock = KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK, false);

		unitListPanel.registerKeyboardAction(
			e -> OnMockPlayers(),
			"Mock",
			mock,
			JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	@Override
	public void pack()
	{
		super.pack();
		setLocationRelativeTo(null);
	}

	protected void OnMockPlayers()
	{
		List<Unit> units = UnitsUtil.createRandomUnitList(true);

		units.forEach(u -> unitListPanel.addUnit(u));
	}

	protected abstract void onWindowOpened();

	protected abstract void onWindowClosed();
	
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
			"Vill du Låsa den nya gruppen?",
			"Låsa grupp?",
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
}
