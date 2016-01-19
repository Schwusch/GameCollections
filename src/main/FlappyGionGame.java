package main;

import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class FlappyGionGame extends BasicGame {
	private Image background = null;
	private Image gion = null;
	private Image wallUp = null;
	private Image wallDown = null;
	private Image wallUp2 = null;
	private Image wallDown2 = null;
	private Shape gionShape = null;
	private Shape[] wallShapes = new Shape[4];
	private int gap = 170;
	private int offset1, offset2;
	private Random rand = new Random();
	private int distance = 200;
	private float gravity = (float) 0.002, gionSpeed = 0, wallSpeed = (float) 0.15;
	private boolean releasedSpace = true, alive = true, start = false;
	private TrueTypeFont ttf;

	public FlappyGionGame(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		URL bgurl = BreakoutGame.class.getResource("/resources/background.png");
		URL gionurl = BreakoutGame.class.getResource("/resources/gion_2.png");
		URL upurl = BreakoutGame.class.getResource("/resources/top.png");
		URL downurl = BreakoutGame.class.getResource("/resources/bottom.png");
		try {
			background = new Image(bgurl.openStream(), "background", false);
			gion = new Image(gionurl.openStream(), "Gion", false);
			wallUp = new Image(downurl.openStream(), "up", false);
			wallDown = new Image(upurl.openStream(), "down", false);
			wallUp2 = new Image(downurl.openStream(), "up", false);
			wallDown2 = new Image(upurl.openStream(), "down", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		gion.setFilter(Image.FILTER_NEAREST);
		gion = gion.getScaledCopy((float) 0.35);
		
		gionShape = new Circle((float) 200, (float) 350,
				(float) gion.getWidth() / 2);
		offset1 = (rand.nextInt(221) - 110);
		offset2 = (rand.nextInt(221) - 110);
		wallShapes[0] = new Rectangle((float) 400, (float) 0 - gap + offset1,
				(float) wallDown.getWidth(), (float) wallDown.getHeight());
		wallShapes[1] = new Rectangle((float) 400, (float) 360 + gap + offset1,
				(float) wallUp.getWidth(), (float) wallUp.getHeight());
		wallShapes[2] = new Rectangle((float) 700, (float) 0 - gap + offset2,
				(float) wallDown.getWidth(), (float) wallDown.getHeight());
		wallShapes[3] = new Rectangle((float) 700, (float) 360 + gap + offset2,
				(float) wallUp.getWidth(), (float) wallUp.getHeight());
		Font font = new Font("Sans Serif", Font.BOLD, 30);
		ttf = new TrueTypeFont(font, false);
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		background.draw(0, 0);
		gion.drawCentered(gionShape.getCenterX(), gionShape.getCenterY());
		wallDown.draw(wallShapes[0].getX(), wallShapes[0].getY());
		wallUp.draw(wallShapes[1].getX(), wallShapes[1].getY());
		wallDown2.draw(wallShapes[2].getX(), wallShapes[2].getY());
		wallUp2.draw(wallShapes[3].getX(), wallShapes[3].getY());
		int points = (distance - 200) / 200;
		g.setFont(ttf);
		g.drawString("" + points, 10, 10);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		if (container.getInput().isKeyDown(Input.KEY_SPACE) && !start) {
			start = true;
		}
		if (alive && start) {
			wallUpdate(delta);
			gionUpdate(container, delta);
			distance += wallSpeed * delta;
		}
		if (start && !alive && container.getInput().isKeyDown(Input.KEY_ENTER)) {
			distance = 200;
			alive = true;
			start = false;
			reset();
		}
	}

	private void wallUpdate(int delta) {
		for (int i = 0; i < wallShapes.length; i++) {
			wallShapes[i].setX((float) (wallShapes[i].getX() - delta * wallSpeed));
			if (wallShapes[i].getX() < -80) {
				wallShapes[i].setX(520);
				if (i < 2) {
					offset1 = (rand.nextInt(221) - 110);
					wallShapes[0].setY((float) 0 - gap + offset1);
					wallShapes[1].setY((float) 360 + gap + offset1);
					
				} else {
					offset2 = (rand.nextInt(221) - 110);
					wallShapes[2].setY((float) 0 - gap + offset2);
					wallShapes[3].setY((float) 360 + gap + offset2);
				}
			}
		}
	}

	private void gionUpdate(GameContainer gc, int delta) {
		Input input = gc.getInput();

		if (input.isKeyDown(Input.KEY_SPACE) && releasedSpace) {
			releasedSpace = false;
			gionSpeed = (float) -0.6;
		} else if (!input.isKeyDown(Input.KEY_SPACE)) {
			releasedSpace = true;
		}

		gionSpeed += gravity * delta;
		float newY = gionShape.getY() + gionSpeed * delta;
		gionShape.setY(newY);

		for (int i = 0; i < 4; i++) {
			if (gionShape.intersects(wallShapes[i])) {
				alive = false;
			}
		}
		if (gionShape.getY() + gionShape.getHeight() > 708) {
			alive = false;
		}
	}
	
	private void reset(){
		gionShape.setY(350);
		offset1 = (rand.nextInt(221) - 110);
		offset2 = (rand.nextInt(221) - 110);
		wallShapes[0].setX((float) 400);
		wallShapes[0].setY((float) 0 - gap + offset1);
		wallShapes[1].setX((float) 400);
		wallShapes[1].setY((float) 360 + gap + offset1);
		wallShapes[2].setX((float) 700);
		wallShapes[2].setY((float) 0 - gap + offset2);
		wallShapes[3].setX((float) 700);
		wallShapes[3].setY((float) 360 + gap + offset2);
	}
}
