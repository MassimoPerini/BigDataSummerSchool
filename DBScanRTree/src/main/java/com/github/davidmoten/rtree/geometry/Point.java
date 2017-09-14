package com.github.davidmoten.rtree.geometry;

import com.github.davidmoten.guavamini.Objects;
import com.github.davidmoten.guavamini.Optional;
import com.github.davidmoten.rtree.internal.util.ObjectsHelper;

public final class Point implements Geometry {

    private final Rectangle mbr;

    private Point(double x, double y) {
        this.mbr = Rectangle.create(x, y, x, y);
    }

    static Point create(double x, double y) {
        return new Point( x,  y);
    }

    static Point create(float x, float y) {
        return new Point(x, y);
    }

    
    public Rectangle mbr() {
        return mbr;
    }

    
    public double distance(Rectangle r) {
        return mbr.distance(r);
    }

    public double distance(Point p) {
        return Math.sqrt(distanceSquared(p));
    }

    public double distanceSquared(Point p) {
    	double dx = mbr().x1() - p.mbr().x1();
    	double dy = mbr().y1() - p.mbr().y1();
        return dx * dx + dy * dy;
    }

    
    public boolean intersects(Rectangle r) {
        return mbr.intersects(r);
    }

    public double x() {
        return mbr.x1();
    }

    public double y() {
        return mbr.y1();
    }

    
    public int hashCode() {
        return Objects.hashCode(mbr);
    }

    
    public boolean equals(Object obj) {
        Optional<Point> other = ObjectsHelper.asClass(obj, Point.class);
        if (other.isPresent()) {
            return Objects.equal(mbr, other.get().mbr());
        } else
            return false;
    }

    
    public String toString() {
        return "Point [x=" + x() + ", y=" + y() + "]";
    }

}