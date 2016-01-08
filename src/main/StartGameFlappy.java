package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class StartGameFlappy {
	public static void main(String[] arguments) {
		try {
			AppGameContainer app = new AppGameContainer(new FlappyGionGame("FlappyGion"));
			app.setDisplayMode(400, 708, false);
			app.setTargetFrameRate(100);
			app.setShowFPS(false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
