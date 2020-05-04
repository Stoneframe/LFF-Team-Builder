package gui.registrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;

import gui.components.LffButton;
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

	private final DefaultListModel<Player> leftListModel;
	private final DefaultListModel<Player> rightListModel;

	private final LffList<Player> leftList;
	private final LffList<Player> rightList;

	private final LffButton moveRightButton;
	private final LffButton moveLeftButton;

	private final LffButton okButton;

	public SplitPanel(Unit unit)
	{
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

		LffPanel centerButtonPanel = new LffPanel(new BorderLayout());

		centerButtonPanel.setBorder(BorderFactory.createEmptyBorder(160, 20, 160, 20));
		centerButtonPanel.add(moveRightButton, BorderLayout.NORTH);
		centerButtonPanel.add(moveLeftButton, BorderLayout.SOUTH);

		LffPanel bottomButtonPanel = new LffPanel(new FlowLayout(FlowLayout.RIGHT));

		bottomButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		bottomButtonPanel.add(okButton);

		unit.getPlayers().forEach(p -> leftListModel.addElement(p));

		setLayout(new BorderLayout());

		add(centerButtonPanel, BorderLayout.CENTER);
		add(new LffScrollPane(leftList, new Dimension(300, 400)), BorderLayout.WEST);
		add(new LffScrollPane(rightList, new Dimension(300, 400)), BorderLayout.EAST);
		add(bottomButtonPanel, BorderLayout.SOUTH);
	}

	public Unit getUnit1()
	{
		return getUnit(leftListModel);
	}

	public Unit getUnit2()
	{
		return getUnit(rightListModel);
	}

	public void addOkButtonActionListener(ActionListener listener)
	{
		okButton.addActionListener(listener);
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

	private static Unit getUnit(DefaultListModel<Player> leftListModel)
	{
		return leftListModel.size() == 1
				? leftListModel.get(0)
				: new Group(
					IntStream.range(0, leftListModel.size())
						.mapToObj(leftListModel::get)
						.collect(Collectors.toList()));
	}
}
