package gui.generator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import gui.components.LffButton;
import gui.components.LffPanel;
import gui.components.LffProgressBar;
import gui.components.LffScrollPane;
import gui.components.LffTextArea;
import teamsbuilder.evolution.ProgressListener;

public class ProgressFrame
	extends JFrame
	implements
		ProgressListener
{
	private static final long serialVersionUID = 2932181298529391658L;

	private final LffPanel mainPanel;
	private final LffPanel buttonPanel;

	private final LffProgressBar progressBar;

	private final LffTextArea detailsTextArea;
	private final LffScrollPane detailsScrollPane;

	private final LffButton detailsButton;

	public ProgressFrame()
	{
		super("Framsteg");

		progressBar = new LffProgressBar();
		progressBar.setPreferredSize(new Dimension(304, 30));

		detailsTextArea = new LffTextArea();
		detailsTextArea.setEditable(false);
		detailsTextArea.setPreferredSize(new Dimension(300, 250));
		detailsTextArea.setFont(new Font("monospaced", Font.PLAIN, 12));

		detailsScrollPane = new LffScrollPane(detailsTextArea);
		detailsScrollPane.setVisible(false);

		detailsButton = new LffButton("Detaljer");
		detailsButton.setEnabled(false);
		detailsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (detailsScrollPane.isVisible())
				{
					detailsScrollPane.setVisible(false);
					detailsButton.setText("Detaljer");
				}
				else
				{
					detailsScrollPane.setVisible(true);
					detailsButton.setText("Dölj");
				}

				pack();
			}
		});

		buttonPanel = new LffPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(detailsButton);

		mainPanel = new LffPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		mainPanel.add(progressBar, BorderLayout.NORTH);
		mainPanel.add(detailsScrollPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setContentPane(mainPanel);
		setResizable(false);
		pack();
	}

	@Override
	public void progressChanged(int percent)
	{
		progressBar.setValue(percent);

		if (percent == 100)
		{
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			detailsButton.setEnabled(true);
		}
	}

	public void setDetails(String details)
	{
		detailsTextArea.setText(details);
	}
}
