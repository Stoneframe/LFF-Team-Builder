package gui.components;

import java.awt.Component;
import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import gui.Util;

public class LffList<T>
	extends JList<T>
{
	private static final long serialVersionUID = 4840074053949256631L;

	public LffList(ListModel<T> listmodel)
	{
		super(listmodel);

		setFont(new Font("Dialog", Font.PLAIN, 20));

		setForeground(Util.FOREGROUND);
		setBackground(Util.BACKGROUND);

		setCellRenderer(new ListCellRenderer<T>()
		{
			@Override
			public Component getListCellRendererComponent(
				JList<? extends T> list,
				T value,
				int index,
				boolean isSelected,
				boolean cellHasFocus)
			{
				JTextArea cell = new JTextArea(value.toString());

				cell.setFont(new Font("Dialog", Font.BOLD, 20));

				if (isSelected)
				{
					cell.setForeground(Util.BACKGROUND);
					cell.setBackground(Util.FOREGROUND);
				}
				else
				{
					cell.setForeground(Util.FOREGROUND);

					if (index % 2 == 1)
					{
						cell.setBackground(Util.MILD_BACKGROUND);
					}
					else
					{
						cell.setBackground(Util.BACKGROUND);
					}
				}

				return cell;
			}
		});
	}

	public T getElementAt(int index)
	{
		return getModel().getElementAt(index);
	}

	public int nbrOfElemets()
	{
		return getModel().getSize();
	}

	public List<T> getAllElements()
	{
		return IntStream.range(0, getModel().getSize())
			.mapToObj(getModel()::getElementAt)
			.collect(Collectors.toList());
	}
}
