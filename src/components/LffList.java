package components;

import java.awt.Font;

import javax.swing.JList;
import javax.swing.ListModel;

import registrator.Util;

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
	}
}
