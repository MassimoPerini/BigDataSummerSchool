package nearestRoad;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import watford.util.quadtree.AbstractKineticObject;
import watford.util.quadtree.AbstractPolyline;
import watford.util.quadtree.AbstractSpatialSet;
import watford.util.quadtree.IPaintable;

public class Car extends AbstractKineticObject implements IPaintable {
	private Point loc;
	private String id;
	
	public String getId() {
		return id;
	}

	public Car(Point loc, String id) {
		this(loc, null, id);
	}

	public Car(Point loc, AbstractSpatialSet parent, String id) {
		super(parent);
		this.loc = loc;
		this.id=id;
	}
	
	public int getX( ) {
		return this.loc.x;
	}
	public int getY( ) {
		return this.loc.y;	
	}
	public Point getPoint( ) {
		return new Point(this.loc);
	}
	public void setPoint(Point p) {
		this.translate(p);
	}
	public void translate(Point p) {
		this.loc = p;
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
	}
	public void translate(int dx, int dy) {
		this.loc.translate(dx,dy);
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
	}

	public boolean intersects(GeneralPath path) {
		return path.intersects(new Rectangle(this.loc.x,this.loc.y,1,1));
	}
	
	public boolean intersects(Rectangle rect) {
		return rect.contains(this.loc);
	}
	
	public boolean intersects(Point pnt) {
		return (this.loc.distance(pnt) == 0.0);
	}
	
	public boolean containedFullyBy(Rectangle rect) {
		return rect.contains(this.loc);
	}

	public boolean containedPartiallyBy(Rectangle rect) {
		return rect.contains(this.loc);
	}
	
	public Color getColor() {
		return Color.red;
	}

	public void paint(Graphics g, Rectangle area, boolean dontColor) {
		if(!dontColor)
			g.setColor(getColor());
		
		if(area.contains(this.loc))
			g.fillOval(this.loc.x, this.loc.y, 2, 2);
	}
	
	public Rectangle getBounds( ) {
		return new Rectangle(this.loc.x, this.loc.y, 0, 0);
	}

	public int pointCount() {
		return 1;
	}

	public Point centroid() {
		return this.loc;
	}
	
	public GeneralPath points() {
		return null;
	}

	public void warpTo(int x, int y) {
		this.loc.x = x;
		this.loc.y = y;
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
	}
}