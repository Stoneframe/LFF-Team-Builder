package gui.components;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gui.Util;

public class LffTextField
	extends JTextField
{
	private static final long serialVersionUID = -8203369786318762329L;

	private List<ActionListener> textListeners = new LinkedList<>();

	public LffTextField()
	{
		super();

		initialize();
	}

	public LffTextField(int columns)
	{
		super(columns);

		initialize();
	}

	private void initialize()
	{
		setForeground(Util.FOREGROUND);
		setBackground(Util.BACKGROUND);

		setFont(new Font("Dialog", Font.PLAIN, 20));
		setBorder(BorderFactory.createLineBorder(Util.FOREGROUND));

		getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				onTextChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				onTextChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				onTextChanged();
			}
		});
	}

	public void addTextListener(ActionListener listener)
	{
		textListeners.add(listener);
	}

	private void onTextChanged()
	{
		ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");

		textListeners.forEach(l -> l.actionPerformed(actionEvent));
	}
}
