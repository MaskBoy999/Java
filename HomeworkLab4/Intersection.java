package org.example;
import java.util.Objects;

public class Intersection implements Comparable<Intersection>{
    private final String name;

    public Intersection(String name){
        this.name=name;
    }

    public String GetName(){
        return this.name;
    }

    @Override
    public String toString(){
        return name;
    }

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
