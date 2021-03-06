package gui.util;

import java.awt.Color;

public class Util
{
	public static Color FOREGROUND = new Color(0, 131, 87);

	public static Color BACKGROUND = Color.WHITE;

	public static Color MILD_BACKGROUND = new Color(184, 228, 199);

	public static boolean isNumeric(String strNum)
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
