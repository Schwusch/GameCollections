package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class StartGame {

	public static void main(String[] arguments) {
		try {
			AppGameContainer app = new AppGameContainer(new BreakoutGame("All hail GION!"));
			app.setDisplayMode(500, 400, false);
			app.setTargetFrameRate(100);
			app.setShowFPS(false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
