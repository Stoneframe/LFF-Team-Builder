package gui.generator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import gui.components.LffButton;
import gui.components.LffLabel;
import gui.components.LffPanel;
import gui.components.LffTextArea;
import gui.components.LffTextField;
import teamsbuilder.TeamsBuilder;

public class SettingsPanel
	extends LffPanel
{
	private static final long serialVersionUID = 6423679252644900636L;

	private final LffLabel titleLabel;

	private final LffLabel nbrOfPlayersLabel;
	private final LffTextField nbrOfPlayersTextField;

	private final LffLabel nbrOfTeamsLabel;
	private final LffTextField nbrOfTeamsTextField;

	private final LffLabel playersPerTeamLabel;
	private final LffLabel minPlayersPerTeamLabel;
	private final LffTextField minPlayersPerTeamTextField;
	private final LffLabel maxPlayersPerTeamLabel;
	private final LffTextField maxPlayersPerTeamTextField;

	private final LffLabel teamNamesLabel;
	private final LffTextArea teamNamesTextArea;

	private final LffButton generateButton;

	public SettingsPanel()
	{
		titleLabel = new LffLabel("Inställningar", Font.BOLD, 40);

		nbrOfPlayersLabel = new LffLabel("Antal spelare:");
		nbrOfPlayersTextField = new LffTextField();
		nbrOfPlayersTextField.setEditable(false);

		nbrOfTeamsLabel = new LffLabel("Antal lag:");
		nbrOfTeamsTextField = new LffTextField();
		nbrOfTeamsTextField.addTextListener(l -> onTextChange());

		playersPerTeamLabel = new LffLabel("Spelare per lag:");

		minPlayersPerTeamLabel = new LffLabel("    Min:");
		minPlayersPerTeamTextField = new LffTextField();

		maxPlayersPerTeamLabel = new LffLabel("    Max:");
		maxPlayersPerTeamTextField = new LffTextField();

		teamNamesLabel = new LffLabel("Lagnamn:");
		teamNamesTextArea = new LffTextArea();
		teamNamesTextArea.addTextListener(l -> onTextChange());

		generateButton = new LffButton("Generera");
		generateButton.setEnabled(false);

		setLayout(new BorderLayout());

		LffPanel center = new LffPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.BOTH;

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
		gbc.gridwidth = 2;
		gbc.gridy = 2;
		gbc.gridx = 0;
		center.add(playersPerTeamLabel, gbc);

		gbc.gridwidth = 1;
		gbc.gridy = 3;
		gbc.gridx = 0;
		center.add(minPlayersPerTeamLabel, gbc);

		gbc.gridwidth = 1;
		gbc.gridy = 3;
		gbc.gridx = 1;
		center.add(minPlayersPerTeamTextField, gbc);

		gbc.gridwidth = 1;
		gbc.gridy = 4;
		gbc.gridx = 0;
		center.add(maxPlayersPerTeamLabel, gbc);

		gbc.gridwidth = 1;
		gbc.gridy = 4;
		gbc.gridx = 1;
		center.add(maxPlayersPerTeamTextField, gbc);

		gbc.insets = new Insets(20, 10, 0, 10);
		gbc.gridwidth = 1;
		gbc.gridy = 5;
		gbc.gridx = 0;
		center.add(teamNamesLabel, gbc);

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weighty = 1;
		gbc.gridwidth = 2;
		gbc.gridy = 6;
		gbc.gridx = 0;
		center.add(
			new JScrollPane(
					teamNamesTextArea,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
			gbc);

		gbc.insets = new Insets(10, 10, 20, 10);
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridy = 8;
		gbc.gridx = 1;
		center.add(generateButton, gbc);

		add(titleLabel, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
	}

	public int getNbrOfPlayers()
	{
		return getInteger(nbrOfPlayersTextField);
	}

	public void setNbrOfPlayers(int nbrOfPlayers)
	{
		nbrOfPlayersTextField.setText(Integer.toString(nbrOfPlayers));
	}

	public int getNbrOfTeams()
	{
		return getInteger(nbrOfTeamsTextField);
	}

	public void setNbrOfTeams(int nbrOfTeams)
	{
		nbrOfTeamsTextField.setText(Integer.toString(nbrOfTeams));
	}

	public int getNbrOfTeamNames()
	{
		return getTeamNames().size();
	}

	public TeamsBuilder getTeamsBuilder()
	{
		TeamsBuilder builder = new TeamsBuilder(age -> age < 13 || 50 < age, getTeamNames());

		builder.setMinimumNumberOfPlayers(getInteger(minPlayersPerTeamTextField));
		builder.setMaximumNumberOfPlayers(getInteger(maxPlayersPerTeamTextField));

		builder.setSplitNonLockedGroups(true);

		return builder;
	}

	public void addGenerateButtonActionListener(ActionListener listener)
	{
		generateButton.addActionListener(listener);
	}

	private boolean isFormValid()
	{
		int nbrOfTeams = getNbrOfTeams();
		int nbrOfTeamNames = getNbrOfTeamNames();

		return nbrOfTeams > 1 && nbrOfTeamNames >= nbrOfTeams;
	}

	private List<String> getTeamNames()
	{
		String[] lines = teamNamesTextArea.getText().split("\n");

		return Arrays.stream(lines)
			.filter(l -> l.length() > 0)
			.collect(Collectors.toList());
	}

	private void onTextChange()
	{
		generateButton.setEnabled(isFormValid());
	}

	private static int getInteger(JTextField textField)
	{
		try
		{
			return Integer.parseInt(textField.getText());
		}
		catch (NumberFormatException | NullPointerException e)
		{
			return 0;
		}
	}
}
