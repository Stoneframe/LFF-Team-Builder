package gui.components;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class LffIcon
	extends ImageIcon
{
	private static final long serialVersionUID = -7367757526545472212L;

	private LffIcon(String iconName, int width, int height)
	{
		Image image = getScaledImage(
			new ImageIcon(getClass().getResource(iconName)).getImage(),
			width,
			height);

		setImage(image);
	}

	public static LffIcon getLffLogo()
	{
		return new LffIcon("/LffLogo.jpg", 146, 168);
	}

	public static LffIcon getGreenFootball()
	{
		return new LffIcon("/GreenFootball.png", 15, 15);
	}

	private static Image getScaledImage(Image srcImg, int w, int h)
	{
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(
			RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}
}
