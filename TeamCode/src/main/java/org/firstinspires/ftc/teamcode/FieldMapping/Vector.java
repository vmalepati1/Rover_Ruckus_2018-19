package org.firstinspires.ftc.teamcode.FieldMapping;

public class Vector {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Returns the vector's distance from another vector (thank you, pythagoras)
    public double distanceFrom(Vector other) {
        return Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2));
    }
    public double distanceFromOrigin() {
        return distanceFrom(new Vector(0, 0));
    }

    // Returns the vector sum/difference of this vector and another vector
    public Vector sum(Vector other) {
        return new Vector(x + other.getX(), y + other.getY());
    }
    public Vector changeInPosition(Vector other) {
        return new Vector(x - other.getX(), y - other.getY());
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}