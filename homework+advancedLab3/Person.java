import java.util.*;

public class Person implements Profile, Comparable<Profile> {
    private String name;
    private String address;
    private String birthdate;
    protected Map<Profile, String> relationships = new HashMap<>();

    public Person(String name, String address, String birthdate) {
        this.name = name;
        this.address = address;
        this.birthdate = birthdate;
    }

    public void addRelationship(Profile p, String type) {
        this.relationships.put(p, type);
    }

    @Override
    public String GetName() { return this.name; }

    @Override
    public String GetAddress() { return this.address; }

    @Override
    public Map<Profile, String> getRelationships() { return this.relationships; }

    @Override
    public int compareTo(Profile o) {
        return this.name.compareTo(o.GetName());
    }

    @Override
    public String toString() {
        return "Person: " + name + " (Address: " + address + ", Born: " + birthdate + ")";
    }
}