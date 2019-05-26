package registrator;

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
