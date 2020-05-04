package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import gui.components.LffButton;
import gui.components.LffLabel;
import gui.components.LffList;
import gui.components.LffPanel;
import model.Unit;

public class UnitListPanel
	extends LffPanel
{
	private static final long serialVersionUID = -2290820403313112798L;

	private final LffLabel titleLabel;

	private final DefaultListModel<Unit> unitListModel;
	private final LffList<Unit> unitList;

	private final LffButton editButton;
	private final LffButton removeButton;

	public UnitListPanel(String titel)
	{
		titleLabel = new LffLabel(titel, Font.BOLD, 40);

		unitListModel = new DefaultListModel<>();

		unitList = new LffList<>(unitListModel);
		unitList.addListSelectionListener(l -> onSelectionChanged());
		unitList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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

		editButton = new LffButton("Redigera...", false);
		removeButton = new LffButton("Ta bort", false);

		JScrollPane scrollPane = new JScrollPane(unitList);
		scrollPane.setBorder(BorderFactory.createLineBorder(Util.FOREGROUND, 2));
		scrollPane.setPreferredSize(new Dimension(300, 500));

		LffPanel buttonPanel = new LffPanel(new FlowLayout(FlowLayout.RIGHT));

		buttonPanel.add(editButton);
		buttonPanel.add(removeButton);

		setLayout(new BorderLayout());

		add(titleLabel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
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

	public void replaceUnit(Unit unitToRemove, Unit unitToAdd)
	{
		int index = unitListModel.indexOf(unitToRemove);

		unitListModel.remove(index);
		unitListModel.add(index, unitToAdd);
	}

	public int getNbrOfPlayers()
	{
		return getUnits().stream().mapToInt(u -> u.numberOfPlayers()).sum();
	}

	public void addEditButtonActionListener(ActionListener listener)
	{
		editButton.addActionListener(listener);
	}

	public void addRemoveButtonActionListener(ActionListener listener)
	{
		removeButton.addActionListener(listener);
	}

	public void setEditButtonVisible(boolean isVisible)
	{
		editButton.setVisible(isVisible);
	}

	public void setRemoveButtonVisible(boolean isVisible)
	{
		removeButton.setVisible(isVisible);
	}

	private void onSelectionChanged()
	{
		editButton.setEnabled(unitList.getSelectedValuesList().size() == 1);
		removeButton.setEnabled(!unitList.isSelectionEmpty());
	}
}
