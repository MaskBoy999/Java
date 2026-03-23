package org.example;
import java.util.Objects;
import java.util.Random;

public class Intersection implements Comparable<Intersection>{
    private final String name;
    private double x, y;

    public Intersection(String name) {
        this.name = name;
        Random rand = new Random();
        this.x = rand.nextInt(10000);
        this.y = rand.nextInt(10000);
    }

    public Intersection(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public String getName() { return name; }

    @Override
    public String toString() { return name + " (" + (int)x + "," + (int)y + ")"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intersection that = (Intersection) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name); }

    @Override
    public int compareTo(Intersection other) {
        return this.name.compareTo(other.name);
    }
}
