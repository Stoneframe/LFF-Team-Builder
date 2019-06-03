package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import model.Unit;
import util.UnitsUtil;

public abstract class LffFrameBase
	extends JFrame
{
	private static final long serialVersionUID = 4706616327895328641L;

	protected final LundsFFPanel lundsFFPanel;
	protected final UnitListPanel unitListPanel;

	protected LffFrameBase(String title)
	{
		lundsFFPanel = new LundsFFPanel();

		unitListPanel = new UnitListPanel("Spelare");
		unitListPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

		setTitle(title);

		setLayout(new BorderLayout());

		add(lundsFFPanel, BorderLayout.NORTH);
		add(unitListPanel, BorderLayout.WEST);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		addWindowListener(new WindowAdapter()
		{
			public void windowOpened(WindowEvent e)
			{
				LffFrameBase.this.onWindowOpened();
			};

			public void windowClosing(WindowEvent e)
			{
				LffFrameBase.this.onWindowClosed();
			};
		});

		KeyStroke mock = KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK, false);

		unitListPanel.registerKeyboardAction(
			e -> OnMockPlayers(),
			"Mock",
			mock,
			JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	@Override
	public void pack()
	{
		super.pack();
		setLocationRelativeTo(null);
	}

	protected void OnMockPlayers()
	{
		List<Unit> units = UnitsUtil.createRandomUnitList(true);

		units.forEach(u -> unitListPanel.addUnit(u));
	}

	protected abstract void onWindowOpened();

	protected abstract void onWindowClosed();
}
