package nearestRoad;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import watford.util.quadtree.AbstractPolyline;
import watford.util.quadtree.AbstractSpatialSet;
import watford.util.quadtree.IPaintable;

public class Road extends AbstractPolyline implements IPaintable {
	private GeneralPath line;
	private double lastX, lastY;
	public double getLastX() {
		return lastX;
	}

	public double getLastY() {
		return lastY;
	}

	public void setLastY(int lastY) {
		this.lastY = lastY;
	}

	private int lastCount;

	public Road( String id, double len) {
		this.line = new GeneralPath();
		this.lastX = this.lastY = 0;
		this.lastCount = 0;
	}
	
	public Road(int[] xx, int[] yy, int count) {
		this.lastX = xx[count-1];
		this.lastY = yy[count-1];
		this.lastCount = this.count;
		updateGeneralPath();
	}
	
	public Road(AbstractSpatialSet parent) {
		super(parent);
		
		this.line = new GeneralPath();
		this.lastX = this.lastY = 0;
		this.lastCount = 0;
	}
	
	public Road(AbstractSpatialSet parent, int[] xx, int[] yy, int count) {
		super(new AbstractSpatialSet[] { parent },xx,yy,count);

		this.lastX = xx[count-1];
		this.lastY = yy[count-1];
		this.lastCount = this.count;
		updateGeneralPath();
	}
	
	public void add(double xx, double yy) {
		if(lastCount > 0) {
			line.append(new Line2D.Double(lastX, lastY, xx, yy), false);
		}
		
		super.add((int)xx,(int)yy);
		
		lastX = xx;
		lastY = yy;
		lastCount = this.count;
	}

	public void add(Point p) {
		this.add(p.x,p.y);
	}
	
	public void updateGeneralPath( ) {
		this.line = new GeneralPath();
		for(int ii = 1; ii < this.count; ii++) {
			float xx = x[ii-1],
				yy = y[ii-1];
			
			line.append(new Line2D.Float(xx, yy, (float)x[ii], (float)y[ii]), false);
		}
	}
	
	public GeneralPath points() {
		return this.line;
	}
	
	public boolean intersects(GeneralPath path) {
		return this.line.intersects(path.getBounds());
	}

	public boolean intersects(Rectangle rect) {
		return this.line.intersects(rect);
	}

	public boolean intersects(Point pnt) {
		return this.line.intersects((double)pnt.x, (double)pnt.y, 1, 1);
	}
	
	public boolean containedFullyBy(Rectangle rect) {
		return rect.contains(this.line.getBounds());
	}
	
	public boolean containedPartiallyBy(Rectangle rect) {
		return this.line.intersects(rect);
	}
	
	public Color getColor() {
		return Color.black;
	}

	public void paint(Graphics g, Rectangle area, boolean dontColor) {
		if(!dontColor)
			g.setColor(getColor());
		
		//XXX cheap trick to see if more than one QuadTreeNode
		//    contains us, which is the only instance we clip
		//    ourselves.
		if(dontColor)
			g.clipRect(area.x, area.y, area.width, area.height);
		
		g.drawPolyline(this.x, this.y, this.count);
		
		if(dontColor)
			g.setClip(null);
	}

	public Rectangle getBounds() {
		return this.line.getBounds();
	}

	public void translate(Point p) {
		// noop on the polyline, its not a kinetic object
	}

	public void translate(int dx, int dy) {
		// noop on the polyline, its not a kinetic object
	}

	public void warpTo(int x, int y) {
		// noop on the polyline, its not a kinetic object
	}

	private static final long serialVersionUID = -8596152720053612015L;
}