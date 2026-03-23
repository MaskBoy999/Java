package org.example;

public class Street implements Comparable<Street> {
    private final String name;
    private final double length;
    private final Intersection from;
    private final Intersection to;

    public Street(String name, double length, Intersection from, Intersection to) {
        this.name = name;
        this.length = length;
        this.from = from;
        this.to = to;
    }

    public Intersection getFrom(){
        return this.from;
    }

    public Intersection getTo(){
        return this.to;
    }

    public String getName() { return name; }
    public double getLength() { return length; }

    @Override
    public String toString() {
        return name + " (" + length + "m) : " + from + " <-> " + to;
    }

    @Override
    public int compareTo(Street other) {
        return Double.compare(this.length, other.length);
    }
}
