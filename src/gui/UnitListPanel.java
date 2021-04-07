package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.ListSelectionModel;

import gui.components.LffButton;
import gui.components.LffLabel;
import gui.components.LffList;
import gui.components.LffPanel;
import gui.components.LffScrollPane;
import model.NumberOf;
import model.Unit;

public class UnitListPanel
	extends LffPanel
{
	private static final long serialVersionUID = -2290820403313112798L;

	private final LffLabel titleLabel;

	private final UnitListModel unitListModel;
	private final LffList<Unit> unitList;

	private final LffButton editButton;
	private final LffButton splitButton;
	private final LffButton mergeButton;
	private final LffButton removeButton;

	public UnitListPanel(String titel, UnitListModel unitListModel)
	{
		this.unitListModel = unitListModel;

		titleLabel = new LffLabel(titel, Font.BOLD, 40);

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
		return unitList.getAllElements();
	}

	public Unit getSelectedUnit()
	{
		return unitList.getSelectedValue();
	}

	public List<Unit> getSelectedUnits()
	{
		return unitList.getSelectedValuesList();
	}

	public void addUnit(Unit unit)
	{
		unitListModel.add(unit);
	}

	public void removeUnit(Unit unit)
	{
		unitListModel.remove(unit);
	}

	public void replaceUnit(Unit unitToRemove, Unit unitToAdd)
	{
		replaceUnit(Arrays.asList(unitToRemove), Arrays.asList(unitToAdd));
	}

	public void replaceUnit(Unit unitToRemove, List<Unit> unitsToAdd)
	{
		replaceUnit(Arrays.asList(unitToRemove), unitsToAdd);
	}

	public void replaceUnit(List<Unit> unitsToRemove, Unit unitToAdd)
	{
		replaceUnit(unitsToRemove, Arrays.asList(unitToAdd));
	}

	public void replaceUnit(List<Unit> unitsToRemove, List<Unit> unitsToAdd)
	{
		unitListModel.replace(unitsToRemove, unitsToAdd);

		int index = unitsToAdd.stream()
			.mapToInt(unit -> unitListModel.indexOf(unit))
			.min()
			.getAsInt();

		unitList.setSelectedIndices(IntStream.range(index, index + unitsToAdd.size()).toArray());
		unitList.ensureIndexIsVisible(index);
	}

	public int getNbrOfPlayers()
	{
		return getUnits().stream().mapToInt(u -> u.count(NumberOf.PLAYERS)).sum();
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

	public void setSplitButtonVisible(boolean isVisible)
	{
		splitButton.setVisible(isVisible);
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
		splitButton.setEnabled(
			unitList.getSelectedValuesList().size() == 1
				&& unitList.getSelectedValue().count(NumberOf.PLAYERS) > 1);
		mergeButton.setEnabled(unitList.getSelectedValuesList().size() > 1);
		removeButton.setEnabled(!unitList.isSelectionEmpty());
	}
}
