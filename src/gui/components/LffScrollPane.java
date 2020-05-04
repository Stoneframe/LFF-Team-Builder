package gui.components;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import gui.Util;

public class LffScrollPane
	extends JScrollPane
{
	private static final long serialVersionUID = -3450087759986457841L;

	public LffScrollPane(JList<?> list, Dimension dimension)
	{
		super(list);

		setBorder(BorderFactory.createLineBorder(Util.FOREGROUND, 2));
		setPreferredSize(dimension);
		setBackground(Util.BACKGROUND);

		getVerticalScrollBar().setBackground(Util.BACKGROUND);
		getHorizontalScrollBar().setBackground(Util.BACKGROUND);

		getVerticalScrollBar().setUI(new BasicScrollBarUI()
		{
			@Override
			protected void configureScrollBarColors()
			{
				this.thumbColor = Util.FOREGROUND;
			}

			@Override
			protected JButton createDecreaseButton(int orientation)
			{
				return createZeroButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation)
			{
				return createZeroButton();
			}

			private JButton createZeroButton()
			{
				JButton button = new JButton("zero button");
				Dimension zeroDim = new Dimension(0, 0);
				button.setPreferredSize(zeroDim);
				button.setMinimumSize(zeroDim);
				button.setMaximumSize(zeroDim);
				return button;
			}
		});
	}
}
