package main;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class BreakoutGame extends BasicGame {
	private float[][] blocksX = new float[13][3], blocksY = new float[13][3];
	private final float blockStartX = 20, blockStartY = 20;
	private float ballVelX, ballVelY;
	private float barX, barY, ballX, ballY;

	private SpriteSheet blocksMixed, blocksSingle, balls, bars;
	private Image[][] blocks = new Image[13][3];
	private Image bar, ball;
	private boolean start = false, loose = false, blocksLeft = true;

	public BreakoutGame(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {

		URL url = BreakoutGame.class.getResource(
                "/resources/breakout_pieces.png");
		URL url2 = BreakoutGame.class.getResource(
                "/resources/gion.png");
		Image spriteImage = null;
		Image gion = null;
		try {
			spriteImage = new Image(url.openStream(), "Textures", false);
			gion = new Image(url2.openStream(), "Gion", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		gion.setFilter(Image.FILTER_NEAREST);
		gion = gion.getScaledCopy((float) 0.4);
		blocksMixed = new SpriteSheet(spriteImage.getSubImage(48, 8, 295, 63),
				31, 15, 5);
		blocksSingle = new SpriteSheet(spriteImage.getSubImage(8, 8, 39, 143),
				31, 15, 5);
		/*balls = new SpriteSheet(spriteImage.getSubImage(48, 136, 62, 8), 8, 8,
				1);*/
		ball = gion;
		bars = new SpriteSheet(spriteImage.getSubImage(8, 151, 275, 194 - 151),
				64, 20, 5);
		reset();
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 3; j++) {
				if (blocks[i][j] != null) {
					blocks[i][j].draw(blocksX[i][j], blocksY[i][j]);
				}
			}
		}
		bar.drawCentered(barX, barY);
		ball.drawCentered(ballX, ballY);

		if (loose && blocksLeft) {
			g.drawString("Press enter to restart", 150, 200);
		}else if(!blocksLeft){
			g.drawString("You won! Press enter to restart", 150, 200);
		}else if(!start && !loose){
			g.drawString("Press space to play the Gion game!", 150, 200);
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		Input input = container.getInput();

		if (input.isKeyDown(Input.KEY_SPACE) && !start) {
			start = true;
			ballX = barX;
			ballY = (float) (barY - bar.getHeight()*2.5);
			ballVelY = (float) -0.1;

		} else if (input.isKeyDown(Input.KEY_ENTER) && loose) {
			reset();
		} else if (start && !loose) {
			blocksLeft = false;

			for (int i = 0; i < 13; i++) {
				for (int j = 0; j < 3; j++) {
					if(blocks[i][j] != null){
						blocksLeft = true;
					}
					// Ball hits on either blocks side
					if (blocksY[i][j] + 15 > ballY
							&& blocksY[i][j] < ballY
							&& ((blocksX[i][j] > ballX && blocksX[i][j] < ballX + ball.getWidth()/2) || blocksX[i][j] + 31 < ballX
									&& blocksX[i][j] + 31 > ballX - ball.getWidth()/2)) {
						// Change X direction
						ballVelX *= -1;
						// "Erase" block
						blocks[i][j] = null;
						blocksY[i][j] = -10;
						blocksX[i][j] = -10;
						// Ball hits on blocks top or bottom
					} else if (blocksX[i][j] < ballX
							&& blocksX[i][j] + 31 > ballX
							&& ((blocksY[i][j] > ballY && blocksY[i][j] < ballY + ball.getHeight()/2) || blocksY[i][j] + 15 < ballY
									&& blocksY[i][j] + 15 > ballY - ball.getHeight()/2)) {
						// Change Y direction
						ballVelY *= -1;
						// "Erase" block
						blocks[i][j] = null;
						blocksY[i][j] = -10;
						blocksX[i][j] = -10;
					}
				}
			}
			// Ball hits ceiling
			if (ballY - ball.getHeight()/2 < 0) {
				ballVelY *= -1;
				// Ball hits walls
			} else if (ballX - ball.getWidth()/2 < 0 || ballX + ball.getWidth()/2 > 500) {
				ballVelX *= -1;
				// Ball hits floor
			} else if (ballY + ball.getHeight()/2 > 400) {
				loose = true;
				// Ball hits bar
			}else if (ballY + ball.getHeight()/2 > barY - 10
					&& (ballX > barX - 32 && ballX < barX + 32)) {

				ballVelX = (float) ((ballX - barX) * 0.005);
				ballVelY *= -1;

			}
			ballY += ballVelY * delta;
			ballX += ballVelX * delta;
		}
		if (input.isKeyDown(Input.KEY_LEFT) && barX - bar.getWidth()/2 > 0){
			barX -= 0.2 * delta;
		} else if (input.isKeyDown(Input.KEY_RIGHT) && barX + bar.getWidth()/2 < 500){
			barX += 0.2 * delta;
		}
		
		if(!blocksLeft){
			loose = true;
		}
	}

	public void reset() {
		Random rand = new Random();
		bar = bars.getSprite(rand.nextInt(3), rand.nextInt(2));
		//ball = balls.getSprite(rand.nextInt(7), 0);

		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 3; j++) {
				blocks[i][j] = blocksMixed.getSprite(rand.nextInt(7),
						rand.nextInt(3));
				blocksX[i][j] = blockStartX + (i * 5)
						+ (i * blocks[i][j].getWidth());
				blocksY[i][j] = blockStartY + (j * 5)
						+ (j * blocks[i][j].getHeight());
			}
		}
		ballVelX = 0;
		ballVelY = 0;
		barX = 250;
		barY = 370;
		ballX = -100;
		ballY = -100;
		start = false;
		loose = false;
	}

}
