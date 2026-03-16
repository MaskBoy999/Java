package org.example;

public class Street implements Comparable<Street> {
    private final String name;
    private final int length;
    private final Intersection from;
    private final Intersection to;

    public Street(String name, int length, Intersection from, Intersection to) {
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
    public int getLength() { return length; }

    @Override
    public String toString() {
        return name + " (" + length + "m) : " + from + " <-> " + to;
    }

    @Override
    public int compareTo(Street other) {
        return Integer.compare(this.length, other.length);
    }
}
