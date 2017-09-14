package com.github.davidmoten.rtree.geometry;

import com.github.davidmoten.guavamini.Objects;
import com.github.davidmoten.guavamini.Optional;
import com.github.davidmoten.guavamini.Preconditions;
import com.github.davidmoten.rtree.internal.util.ObjectsHelper;

public final class Rectangle implements Geometry, HasGeometry {
    private final double x1, y1, x2, y2;

    private Rectangle(double x1, double y1, double x2, double y2) {
        Preconditions.checkArgument(x2 >= x1);
        Preconditions.checkArgument(y2 >= y1);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    static Rectangle create(double x1, double y1, double x2, double y2) {
        return new Rectangle( x1,  y1,  x2, y2);
    }

    static Rectangle create(float x1, float y1, float x2, float y2) {
        return new Rectangle(x1, y1, x2, y2);
    }

    public double x1() {
        return x1;
    }

    public double y1() {
        return y1;
    }

    public double x2() {
        return x2;
    }

    public double y2() {
        return y2;
    }

    public double area() {
        return (x2 - x1) * (y2 - y1);
    }

    public Rectangle add(Rectangle r) {
        return new Rectangle(min(x1, r.x1), min(y1, r.y1), max(x2, r.x2), max(y2, r.y2));
    }

    public boolean contains(double x, double y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    
    public boolean intersects(Rectangle r) {
        return r.x2 >= x1 && r.x1 <= x2 && r.y2 >= y1 && r.y1 <= y2;
    }

    
    public double distance(Rectangle r) {
        if (intersects(r))
            return 0;
        else {
            Rectangle mostLeft = x1 < r.x1 ? this : r;
            Rectangle mostRight = x1 > r.x1 ? this : r;
            double xDifference = max(0,
                    mostLeft.x1 == mostRight.x1 ? 0 : mostRight.x1 - mostLeft.x2);

            Rectangle upper = y1 < r.y1 ? this : r;
            Rectangle lower = y1 > r.y1 ? this : r;

            double yDifference = max(0, upper.y1 == lower.y1 ? 0 : lower.y1 - upper.y2);

            return Math.sqrt(xDifference * xDifference + yDifference * yDifference);
        }
    }

    
    public Rectangle mbr() {
        return this;
    }

    
    public String toString() {
        return "Rectangle [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + "]";
    }

    
    public int hashCode() {
        return Objects.hashCode(x1, y1, x2, y2);
    }

    
    public boolean equals(Object obj) {
        Optional<Rectangle> other = ObjectsHelper.asClass(obj, Rectangle.class);
        if (other.isPresent()) {
            return Objects.equal(x1, other.get().x1) && Objects.equal(x2, other.get().x2)
                    && Objects.equal(y1, other.get().y1) && Objects.equal(y2, other.get().y2);
        } else
            return false;
    }

    public double intersectionArea(Rectangle r) {
        if (!intersects(r))
            return 0;
        else
            return create(max(x1, r.x1), max(y1, r.y1), min(x2, r.x2), min(y2, r.y2)).area();
    }

    public double perimeter() {
        return 2 * (x2 - x1) + 2 * (y2 - y1);
    }

    
    public Geometry geometry() {
        return this;
    }

    private static double max(double a, double b) {
        if (a < b)
            return b;
        else
            return a;
    }

    private static double min(double a, double b) {
        if (a < b)
            return a;
        else
            return b;
    }

}