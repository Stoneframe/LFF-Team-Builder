package gui.generator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

import gui.Util;
import gui.components.LffButton;
import gui.components.LffLabel;
import gui.components.LffPanel;
import gui.components.LffTextField;

public class SettingsPanel
	extends LffPanel
{
	private static final long serialVersionUID = 6423679252644900636L;

	private final LffLabel titleLabel;

	private final LffLabel nbrOfPlayersLabel;
	private final LffTextField nbrOfPlayersTextField;

	private final LffLabel nbrOfTeamsLabel;
	private final LffTextField nbrOfTeamsTextField;

	private final LffLabel teamNamesLabel;
	private final JTextArea teamNamesTextArea;

	private final LffButton generateButton;

	public SettingsPanel()
	{
		titleLabel = new LffLabel("Inställningar", Font.BOLD, 40);

		nbrOfPlayersLabel = new LffLabel("Antal spelare:");
		nbrOfPlayersTextField = new LffTextField(5);
		nbrOfPlayersTextField.setEditable(false);

		nbrOfTeamsLabel = new LffLabel("Antal lag:");
		nbrOfTeamsTextField = new LffTextField(5);

		teamNamesLabel = new LffLabel("Lag namn:");

		teamNamesTextArea = new JTextArea(15, 25);
		teamNamesTextArea.setBorder(BorderFactory.createLineBorder(Util.FOREGROUND, 1));

		generateButton = new LffButton("Generera");

		setLayout(new BorderLayout());

		LffPanel center = new LffPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.NORTHWEST;

		gbc.gridwidth = 1;
		gbc.gridy = 0;
		gbc.gridx = 0;
		center.add(nbrOfPlayersLabel, gbc);

		gbc.gridwidth = 1;
		gbc.gridy = 0;
		gbc.gridx = 1;
		center.add(nbrOfPlayersTextField, gbc);

		gbc.gridwidth = 1;
		gbc.gridy = 1;
		gbc.gridx = 0;
		center.add(nbrOfTeamsLabel, gbc);

		gbc.gridwidth = 1;
		gbc.gridy = 1;
		gbc.gridx = 1;
		center.add(nbrOfTeamsTextField, gbc);

		gbc.insets = new Insets(10, 10, 0, 10);
		gbc.gridwidth = 1;
		gbc.gridy = 2;
		gbc.gridx = 0;
		center.add(teamNamesLabel, gbc);

		gbc.insets = new Insets(0, 10, 10, 10);
		gbc.gridwidth = 2;
		gbc.gridy = 3;
		gbc.gridx = 0;
		center.add(teamNamesTextArea, gbc);

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridy = 5;
		gbc.gridx = 1;
		center.add(generateButton, gbc);

		gbc.gridwidth = 2;
		gbc.weighty = 1.0;
		gbc.gridy = 6;
		gbc.gridx = 0;
		center.add(new LffPanel(), gbc);

		add(titleLabel, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
	}

	public int getNbrOfPlayers()
	{
		return Integer.parseInt(nbrOfPlayersTextField.getText());
	}

	public void setNbrOfPlayers(int nbrOfPlayers)
	{
		nbrOfPlayersTextField.setText(Integer.toString(nbrOfPlayers));
	}

	public int getNbrOfTeams()
	{
		return Integer.parseInt(nbrOfTeamsTextField.getText());
	}

	public void setNbrOfTeams(int nbrOfTeams)
	{
		nbrOfTeamsTextField.setText(Integer.toString(nbrOfTeams));
	}

	public void addGenerateButtonActionListener(ActionListener listener)
	{
		generateButton.addActionListener(listener);
	}
}
