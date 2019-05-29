package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import gui.components.LffButton;
import gui.components.LffLabel;
import gui.components.LffList;
import gui.components.LffPanel;
import model.Unit;
import util.UnitsUtil;

public class UnitListPanel
	extends LffPanel
{
	private static final long serialVersionUID = -2290820403313112798L;

	private final LffLabel titleLabel;

	private final DefaultListModel<Unit> unitListModel;
	private final LffList<Unit> unitList;

	private final LffButton removeButton;

	public UnitListPanel(String titel)
	{
		titleLabel = new LffLabel(titel, Font.BOLD, 40);

		unitListModel = new DefaultListModel<>();

		unitList = new LffList<>(unitListModel);
		unitList.addListSelectionListener(l -> onSelectionChanged());
		unitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		unitList.setCellRenderer(new ListCellRenderer<Unit>()
		{
			@Override
			public Component getListCellRendererComponent(
					JList<? extends Unit> list,
					Unit value,
					int index,
					boolean isSelected,
					boolean cellHasFocus)
			{
				JTextArea cell = new JTextArea(value.toString());

				cell.setFont(new Font("Dialog", Font.BOLD, 20));

				if (isSelected)
				{
					cell.setForeground(Util.BACKGROUND);
					cell.setBackground(Util.FOREGROUND);
				}
				else
				{
					cell.setForeground(Util.FOREGROUND);

					if (index % 2 == 1)
					{
						cell.setBackground(Util.MILD_BACKGROUND);
					}
					else
					{
						cell.setBackground(Util.BACKGROUND);
					}
				}

				return cell;
			}
		});

		removeButton = new LffButton("Ta bort", false);

		JScrollPane scrollPane = new JScrollPane(unitList);
		scrollPane.setBorder(BorderFactory.createLineBorder(Util.FOREGROUND, 2));
		scrollPane.setPreferredSize(new Dimension(300, 500));

		LffPanel buttonPanel = new LffPanel(new FlowLayout(FlowLayout.RIGHT));

		buttonPanel.add(removeButton);

		setLayout(new BorderLayout());

		add(titleLabel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		KeyStroke mock = KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK, false);

		registerKeyboardAction(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				List<Unit> units = UnitsUtil.createRandomUnitList(true);

				units.forEach(u -> addUnit(u));
			}
		}, "Mock", mock, JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public List<Unit> getUnits()
	{
		return Collections.list(unitListModel.elements());
	}

	public Unit getSelectedUnit()
	{
		return unitList.getSelectedValue();
	}

	public void setUnits(List<Unit> units)
	{
		units.forEach(unit -> unitListModel.addElement(unit));
	}

	public void addUnit(Unit unit)
	{
		unitListModel.addElement(unit);
	}

	public void removeUnit(Unit unit)
	{
		unitListModel.removeElement(unit);
	}

	public int getNbrOfPlayers()
	{
		return getUnits().stream().mapToInt(u -> u.numberOfPlayers()).sum();
	}

	public void addRemoveButtonActionListener(ActionListener listener)
	{
		removeButton.addActionListener(listener);
	}

	public void setRemoveButtonVisible(boolean isEnabled)
	{
		removeButton.setVisible(isEnabled);
	}

	private void onSelectionChanged()
	{
		removeButton.setEnabled(!unitList.isSelectionEmpty());
	}
}
