package registrator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import org.junit.platform.commons.util.StringUtils;

import components.LffLabel;
import components.LffPanel;
import components.LffTextField;
import model.Player;

public class PlayerPanel
	extends LffPanel
{
	private static final long serialVersionUID = -5839179580144202007L;

	private final LffLabel nameLabel;
	private final LffTextField nameTextField;

	private final LffLabel ageLabel;
	private final LffTextField ageTextField;

	public PlayerPanel()
	{
		nameLabel = new LffLabel("Namn:");
		nameTextField = new LffTextField(15);

		ageLabel = new LffLabel("Ålder:");
		ageTextField = new LffTextField(5);

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridy = 0;
		gbc.gridx = 0;
		add(nameLabel, gbc);

		gbc.gridy = 0;
		gbc.gridx = 1;
		add(nameTextField, gbc);

		gbc.gridy = 0;
		gbc.gridx = 2;
		add(ageLabel, gbc);

		gbc.gridy = 0;
		gbc.gridx = 3;
		add(ageTextField, gbc);
	}

	public Player getPlayer()
	{
		return new Player(nameTextField.getText(), Integer.parseInt(ageTextField.getText()));
	}

	public boolean isFormValid()
	{
		return StringUtils.isNotBlank(nameTextField.getText())
				&& StringUtils.isNotBlank(ageTextField.getText())
				&& isNumeric(ageTextField.getText());
	}

	public void clear()
	{
		nameTextField.setText("");
		ageTextField.setText("");
	}

	public void addTextLister(ActionListener listener)
	{
		nameTextField.addTextListener(listener);
		ageTextField.addTextListener(listener);
	}

	private static boolean isNumeric(String strNum)
	{
		try
		{
			Double.parseDouble(strNum);
		}
		catch (NumberFormatException | NullPointerException nfe)
		{
			return false;
		}

		return true;
	}
}
