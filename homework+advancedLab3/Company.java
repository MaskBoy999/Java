import java.util.*;

public class Company implements Profile, Comparable<Profile> {
    private String name;
    private String address;
    private int employees;
    protected Map<Profile, String> relationships = new HashMap<>();

    public Company(String name, String address, int employees) {
        this.name = name;
        this.address = address;
        this.employees = employees;
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
        return "Company: " + name + " (Address: " + address + ", Employees: " + employees + ")";
    }
}