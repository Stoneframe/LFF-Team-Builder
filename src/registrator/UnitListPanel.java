package registrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import model.Unit;
import registrator.components.LffButton;
import registrator.components.LffList;
import registrator.components.LffPanel;

public class UnitListPanel
	extends LffPanel
{
	private static final long serialVersionUID = -2290820403313112798L;

	private final DefaultListModel<Unit> unitListModel;
	private final LffList<Unit> unitList;

	private final LffButton removeButton;

	public UnitListPanel()
	{
		unitListModel = new DefaultListModel<>();

		unitList = new LffList<>(unitListModel);
		unitList.addListSelectionListener(l -> onSelectionChanged());
		unitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		removeButton = new LffButton("Ta bort", false);

		JScrollPane scrollPane = new JScrollPane(unitList);
		scrollPane.setBorder(BorderFactory.createLineBorder(Util.FOREGROUND, 2));
		scrollPane.setPreferredSize(new Dimension(300, 500));

		LffPanel buttonPanel = new LffPanel(new FlowLayout(FlowLayout.RIGHT));

		buttonPanel.add(removeButton);

		setLayout(new BorderLayout());

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

	public void addRemoveButtonActionListener(ActionListener listener)
	{
		removeButton.addActionListener(listener);
	}

	private void onSelectionChanged()
	{
		removeButton.setEnabled(!unitList.isSelectionEmpty());
	}
}
