package gui.registrator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.KeyStroke;

import gui.components.LffButton;
import gui.components.LffCheckBox;
import gui.components.LffLabel;
import gui.components.LffPanel;
import model.Group;
import model.NumberOf;
import model.Player;
import model.Unit;

public class FormPanel
	extends LffPanel
{
	public static final int ADD_MODE = 0;
	public static final int SAVE_MODE = 1;

	private static final long serialVersionUID = -8893769532503054139L;

	private final LffLabel titleLabel;

	private final List<PlayerPanel> playerPanels;

	private final LffCheckBox lockCheckBox;

	private final LffButton plusButton;
	private final LffButton minusButton;

	private final LffButton okButton;

	private int nbrOfPlayers = 1;

	public FormPanel(int mode)
	{
		KeyStroke lock = KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK, true);
		KeyStroke add = KeyStroke.getKeyStroke(KeyEvent.VK_ADD, ActionEvent.CTRL_MASK, true);
		KeyStroke sub = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, ActionEvent.CTRL_MASK, true);
		KeyStroke plus = KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK, true);
		KeyStroke minus = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK, true);
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, ActionEvent.CTRL_MASK, true);

		titleLabel = new LffLabel(getTitleText(mode), Font.BOLD, 40);

		playerPanels = Arrays.asList(
			new PlayerPanel(),
			new PlayerPanel(),
			new PlayerPanel(),
			new PlayerPanel(),
			new PlayerPanel(),
			new PlayerPanel());

		playerPanels.forEach(pp -> pp.addTextLister(l -> onTextChanged()));

		lockCheckBox = new LffCheckBox("Lås");
		lockCheckBox.registerKeyboardAction(
			l -> lockCheckBox.doClick(),
			lock,
			JComponent.WHEN_IN_FOCUSED_WINDOW);

		plusButton = new LffButton("+", 15);
		plusButton.setPreferredSize(new Dimension(60, 30));
		plusButton.addActionListener(l -> plusClicked());
		plusButton.setFocusable(false);
		plusButton.registerKeyboardAction(
			e -> plusClicked(),
			add,
			JComponent.WHEN_IN_FOCUSED_WINDOW);
		plusButton.registerKeyboardAction(
			e -> plusClicked(),
			plus,
			JComponent.WHEN_IN_FOCUSED_WINDOW);

		minusButton = new LffButton("-", 15);
		minusButton.setPreferredSize(new Dimension(60, 30));
		minusButton.addActionListener(l -> minusClicked());
		minusButton.setFocusable(false);
		minusButton.registerKeyboardAction(
			e -> minusClicked(),
			sub,
			JComponent.WHEN_IN_FOCUSED_WINDOW);
		minusButton.registerKeyboardAction(
			e -> minusClicked(),
			minus,
			JComponent.WHEN_IN_FOCUSED_WINDOW);

		okButton = new LffButton(getOkButtonText(mode), false);
		okButton.addActionListener(l -> requestFocus());
		okButton.registerKeyboardAction(
			l -> okButton.doClick(),
			enter,
			JComponent.WHEN_IN_FOCUSED_WINDOW);

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
		add(okButton, gbc);

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

	public void addOkButtonActionListener(ActionListener listener)
	{
		okButton.addActionListener(listener);
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

	public void setUnit(Unit unit)
	{
		nbrOfPlayers = unit.count(NumberOf.PLAYERS);

		if (unit instanceof Player)
		{
			playerPanels.get(0).setPlayer((Player)unit);
		}
		else
		{
			Group group = (Group)unit;

			int i = 0;
			for (Player player : group)
			{
				playerPanels.get(i).setPlayer(player);
				i++;
			}

			lockCheckBox.setSelected(group.isLocked());
		}

		update();
	}

	@Override
	public void requestFocus()
	{
		playerPanels.get(0).requestFocus();
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

		updateOkButtonEnabled();
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
		updateOkButtonEnabled();
	}

	private void updateOkButtonEnabled()
	{
		okButton.setEnabled(isFormValid());
	}

	private String getTitleText(int mode)
	{
		switch (mode)
		{
			case SAVE_MODE:
				return "Redigera";

			case ADD_MODE:
			default:
				return "Registrering";
		}
	}

	private String getOkButtonText(int mode)
	{
		switch (mode)
		{
			case SAVE_MODE:
				return "Spara";

			case ADD_MODE:
			default:
				return "Lägg till";
		}
	}
}
