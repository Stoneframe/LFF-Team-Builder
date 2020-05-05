package gui.registrator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;

import gui.components.LffButton;
import gui.components.LffCheckBox;
import gui.components.LffLabel;
import gui.components.LffList;
import gui.components.LffPanel;
import gui.components.LffScrollPane;
import model.Group;
import model.Player;
import model.Unit;

public class SplitPanel
	extends LffPanel
{
	private static final long serialVersionUID = 507340582182135976L;

	private final LffLabel titleLabel;

	private final LffCheckBox leftLockCheckBox;
	private final LffCheckBox rightLockCheckBox;

	private final DefaultListModel<Player> leftListModel;
	private final DefaultListModel<Player> rightListModel;

	private final LffList<Player> leftList;
	private final LffList<Player> rightList;

	private final LffButton moveRightButton;
	private final LffButton moveLeftButton;

	private final LffButton okButton;

	public SplitPanel(Unit unit)
	{
		titleLabel = new LffLabel("Dela", Font.BOLD, 40);

		leftLockCheckBox = new LffCheckBox("Lås");
		rightLockCheckBox = new LffCheckBox("Lås");

		leftListModel = new DefaultListModel<>();
		rightListModel = new DefaultListModel<>();

		leftList = new LffList<Player>(leftListModel);
		leftList.addListSelectionListener(l -> onSelectionChanged());

		rightList = new LffList<Player>(rightListModel);
		rightList.addListSelectionListener(l -> onSelectionChanged());

		moveRightButton = new LffButton(">", 20, false);
		moveRightButton.setPreferredSize(new Dimension(50, 30));
		moveRightButton.addActionListener(l -> onMoveRight());

		moveLeftButton = new LffButton("<", 20, false);
		moveLeftButton.setPreferredSize(new Dimension(50, 30));
		moveLeftButton.addActionListener(l -> onMoveLeft());

		okButton = new LffButton("Spara", false);

		LffScrollPane leftScollPane = new LffScrollPane(leftList, new Dimension(270, 300));
		LffScrollPane rightScrollPane = new LffScrollPane(rightList, new Dimension(270, 300));

		LffPanel buttonPanel = new LffPanel(new GridLayout(2, 1, 10, 10));
		buttonPanel.add(moveRightButton);
		buttonPanel.add(moveLeftButton);

		LffPanel leftListPanel = new LffPanel(new BorderLayout());
		leftListPanel.add(leftLockCheckBox, BorderLayout.NORTH);
		leftListPanel.add(leftScollPane, BorderLayout.CENTER);

		LffPanel rightListPanel = new LffPanel(new BorderLayout());
		rightListPanel.add(rightLockCheckBox, BorderLayout.NORTH);
		rightListPanel.add(rightScrollPane, BorderLayout.CENTER);

		setLayout(new GridBagLayout());

		add(0, 0, 3, 1, GridBagConstraints.FIRST_LINE_START, titleLabel);
		add(0, 1, 1, 3, GridBagConstraints.CENTER, leftListPanel, GridBagConstraints.BOTH);
		add(2, 1, 1, 3, GridBagConstraints.CENTER, rightListPanel, GridBagConstraints.BOTH);
		add(1, 2, 1, 1, GridBagConstraints.CENTER, buttonPanel);
		add(2, 5, 1, 1, GridBagConstraints.LAST_LINE_END, okButton);

		unit.getPlayers().forEach(p -> leftListModel.addElement(p));
	}

	public Unit getUnit1()
	{
		return getUnit(leftList, leftLockCheckBox);
	}

	public Unit getUnit2()
	{
		return getUnit(rightList, rightLockCheckBox);
	}

	public void addOkButtonActionListener(ActionListener listener)
	{
		okButton.addActionListener(listener);
	}

	private void add(
		int gridx,
		int gridy,
		int gridwidth,
		int gridheight,
		int anchor,
		Component component)
	{
		add(gridx, gridy, gridwidth, gridheight, anchor, component, GridBagConstraints.NONE);
	}

	private void add(
		int gridx,
		int gridy,
		int gridwidth,
		int gridheight,
		int anchor,
		Component component,
		int fill)
	{
		add(
			component,
			new GridBagConstraints(
				gridx,
				gridy,
				gridwidth,
				gridheight,
				1,
				1,
				anchor,
				fill,
				new Insets(10, 10, 10, 10),
				0,
				0));
	}

	private void onSelectionChanged()
	{
		moveRightButton.setEnabled(leftList.getSelectedIndices().length > 0);
		moveLeftButton.setEnabled(rightList.getSelectedIndices().length > 0);
		okButton.setEnabled(!rightListModel.isEmpty());
	}

	private void onMoveRight()
	{
		for (Player player : leftList.getSelectedValuesList())
		{
			leftListModel.removeElement(player);
			rightListModel.addElement(player);
		}

		onSelectionChanged();
	}

	private void onMoveLeft()
	{
		for (Player player : rightList.getSelectedValuesList())
		{
			rightListModel.removeElement(player);
			leftListModel.addElement(player);
		}

		onSelectionChanged();
	}

	private static Unit getUnit(LffList<Player> list, LffCheckBox lockCheckBox)
	{
		return list.nbrOfElemets() == 1
				? list.getElementAt(0)
				: new Group(lockCheckBox.isEnabled(), list.getAllElements());
	}
}
