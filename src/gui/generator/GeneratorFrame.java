package gui.generator;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GeneratorFrame
	extends JFrame
{
	private static final long serialVersionUID = 1932555429400080599L;

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new GeneratorFrame();
			}
		});
	}
}
