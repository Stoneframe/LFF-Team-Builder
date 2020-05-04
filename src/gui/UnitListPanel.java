package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;

import gui.components.LffButton;
import gui.components.LffLabel;
import gui.components.LffList;
import gui.components.LffPanel;
import gui.components.LffScrollPane;
import model.Unit;

public class UnitListPanel
	extends LffPanel
{
	private static final long serialVersionUID = -2290820403313112798L;

	private final LffLabel titleLabel;

	private final DefaultListModel<Unit> unitListModel;
	private final LffList<Unit> unitList;

	private final LffButton editButton;
	private final LffButton splitButton;
	private final LffButton mergeButton;
	private final LffButton removeButton;

	public UnitListPanel(String titel)
	{
		titleLabel = new LffLabel(titel, Font.BOLD, 40);

		unitListModel = new DefaultListModel<>();

		unitList = new LffList<>(unitListModel);
		unitList.addListSelectionListener(l -> onSelectionChanged());
		unitList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		editButton = new LffButton("Redigera...", false);
		splitButton = new LffButton("Dela...", false);
		mergeButton = new LffButton("Slå ihop", false);
		removeButton = new LffButton("Ta bort", false);

		LffScrollPane scrollPane = new LffScrollPane(unitList, new Dimension(300, 500));

		LffPanel buttonPanel = new LffPanel(new FlowLayout(FlowLayout.RIGHT));

		buttonPanel.add(editButton);
		buttonPanel.add(splitButton);
		buttonPanel.add(mergeButton);
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

	public List<Unit> getSelectedUnits()
	{
		return unitList.getSelectedValuesList();
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

	// public void replaceUnit(Unit unitToRemove, Unit unitToAdd)
	// {
	// int index = unitListModel.indexOf(unitToRemove);
	//
	// unitListModel.remove(index);
	// unitListModel.add(index, unitToAdd);
	//
	// unitList.setSelectedValue(unitToAdd, true);
	// }

	public void replaceUnit(Unit unitToRemove, Unit... unitsToAdd)
	{
		int index = unitListModel.indexOf(unitToRemove);

		unitListModel.remove(index);

		for (int i = 0; i < unitsToAdd.length; i++)
		{
			unitListModel.add(index + i, unitsToAdd[i]);
		}

		unitList.setSelectedIndices(IntStream.range(index, index + unitsToAdd.length).toArray());
		unitList.ensureIndexIsVisible(index);
	}

	public int getNbrOfPlayers()
	{
		return getUnits().stream().mapToInt(u -> u.numberOfPlayers()).sum();
	}

	public void addEditButtonActionListener(ActionListener listener)
	{
		editButton.addActionListener(listener);
	}

	public void addSplitButtonActionListener(ActionListener listener)
	{
		splitButton.addActionListener(listener);
	}

	public void addMergeButtonActionListener(ActionListener listener)
	{
		mergeButton.addActionListener(listener);
	}

	public void addRemoveButtonActionListener(ActionListener listener)
	{
		removeButton.addActionListener(listener);
	}

	public void setEditButtonVisible(boolean isVisible)
	{
		editButton.setVisible(isVisible);
	}

	public void setMergeButtonVisible(boolean isVisible)
	{
		mergeButton.setVisible(isVisible);
	}

	public void setRemoveButtonVisible(boolean isVisible)
	{
		removeButton.setVisible(isVisible);
	}

	private void onSelectionChanged()
	{
		editButton.setEnabled(unitList.getSelectedValuesList().size() == 1);
		splitButton.setEnabled(unitList.getSelectedValuesList().size() == 1);
		mergeButton.setEnabled(unitList.getSelectedValuesList().size() > 1);
		removeButton.setEnabled(!unitList.isSelectionEmpty());
	}
}
