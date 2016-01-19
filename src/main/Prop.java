package main;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

public class Prop {
	private Shape boundingBox;
	
	public Prop(Shape bounds) {
		boundingBox = bounds;
	}
	
	public Shape getShape() {
		return boundingBox;
	}
	
	public void setShape(Shape shape) {
		boundingBox = shape;
	}
	
	public void draw(Graphics g) {
		g.draw(boundingBox);
	}
	
	public boolean intersects(Shape othershape) {
		return boundingBox.intersects(othershape);
	}
}
