package gui.registrator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import gui.Util;
import gui.components.LffButton;
import gui.components.LffCheckBox;
import gui.components.LffLabel;
import gui.components.LffPanel;
import model.Group;
import model.Player;
import model.Unit;

public class FormPanel
	extends LffPanel
{
	private static final long serialVersionUID = -8893769532503054139L;

	private final LffLabel titleLabel;

	private final List<PlayerPanel> playerPanels;

	private final LffCheckBox lockCheckBox;

	private final LffButton plusButton;
	private final LffButton minusButton;

	private final LffButton addButton;

	private int nbrOfPlayers = 1;

	public FormPanel()
	{
		titleLabel = new LffLabel("Registrering", Font.BOLD, 40);

		playerPanels = Arrays.asList(
			new PlayerPanel(),
			new PlayerPanel(),
			new PlayerPanel(),
			new PlayerPanel(),
			new PlayerPanel(),
			new PlayerPanel());

		playerPanels.forEach(pp -> pp.addTextLister(l -> onTextChanged()));

		lockCheckBox = new LffCheckBox("Lås");
		lockCheckBox.setForeground(Util.FOREGROUND);
		lockCheckBox.setBackground(Util.BACKGROUND);

		plusButton = new LffButton("+", 20);
		plusButton.setPreferredSize(new Dimension(50, 30));
		plusButton.addActionListener(l -> plusClicked());
		plusButton.setFocusable(false);

		minusButton = new LffButton("-", 20);
		minusButton.setPreferredSize(new Dimension(50, 30));
		minusButton.addActionListener(l -> minusClicked());
		minusButton.setFocusable(false);

		addButton = new LffButton("Lägg till", false);

		setLayout(new GridBagLayout());

		lockCheckBox.setVisible(playerPanels.size() > 1);

		LffPanel buttonPanel = new LffPanel(new GridLayout(3, 1, 10, 5));

		buttonPanel.add(plusButton);
		buttonPanel.add(minusButton);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(10, 10, 10, 10);

		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridy = 0;
		gbc.gridx = 1;
		add(lockCheckBox, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridy = playerPanels.size() + 2;
		gbc.gridx = 0;
		add(addButton, gbc);

		gbc.anchor = GridBagConstraints.NORTHWEST;

		gbc.gridy = 0;
		gbc.gridx = 0;
		add(titleLabel, gbc);

		gbc.gridy = 1;
		gbc.gridx = 1;
		gbc.gridheight = 3;
		add(buttonPanel, gbc);

		gbc.gridx = 0;
		gbc.gridheight = 1;

		for (int i = 0; i < playerPanels.size(); i++)
		{
			gbc.gridy = i + 1;
			add(playerPanels.get(i), gbc);
		}

		update();
	}

	public void reset()
	{
		lockCheckBox.setSelected(false);
		nbrOfPlayers = 1;

		for (PlayerPanel playerPanel : playerPanels)
		{
			playerPanel.clear();
		}

		update();
	}

	public void addAddButtonActionListener(ActionListener listener)
	{
		addButton.addActionListener(listener);
	}

	public Unit getUnit()
	{
		if (nbrOfPlayers == 1)
		{
			return playerPanels.get(0).getPlayer();
		}
		else
		{
			List<Player> players = playerPanels.stream()
				.limit(nbrOfPlayers)
				.map(pp -> pp.getPlayer())
				.collect(Collectors.toList());

			return new Group(lockCheckBox.isSelected(), players);
		}
	}

	private void plusClicked()
	{
		nbrOfPlayers++;
		update();
	}

	private void minusClicked()
	{
		nbrOfPlayers--;
		update();
	}

	private void update()
	{
		lockCheckBox.setVisible(nbrOfPlayers > 1);

		plusButton.setEnabled(nbrOfPlayers < playerPanels.size());
		minusButton.setEnabled(nbrOfPlayers > 1);

		for (int i = 0; i < playerPanels.size(); i++)
		{
			playerPanels.get(i).setVisible(i < nbrOfPlayers);
		}
	}

	private boolean isFormValid()
	{
		return playerPanels.stream()
			.limit(nbrOfPlayers)
			.map(pp -> pp.isFormValid())
			.allMatch(b -> b == true);
	}

	private void onTextChanged()
	{
		addButton.setEnabled(isFormValid());
	}
}
