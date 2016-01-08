package main;

import java.io.IOException;
import java.net.URL;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class FlappyGionGame extends BasicGame{
	private Image background = null;
	private Image gion = null;
	private Image wallUp = null;
	private Image wallDown = null;

	public FlappyGionGame(String title) {
		super(title);
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		URL bgurl = BreakoutGame.class.getResource(
                "/resources/background.png");
		URL gionurl = BreakoutGame.class.getResource(
                "/resources/gion.png");
		URL upurl = BreakoutGame.class.getResource(
                "/resources/top.png");
		URL downurl = BreakoutGame.class.getResource(
                "/resources/bottom.png");
		try {
			background = new Image(bgurl.openStream(), "background", false);
			gion = new Image(gionurl.openStream(), "Gion", false);
			wallUp = new Image(downurl.openStream(), "up", false);
			wallDown = new Image(downurl.openStream(), "down", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		gion.setFilter(Image.FILTER_NEAREST);
		gion = gion.getScaledCopy((float) 0.4);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		
	}

}
