import java.util.Comparator;
import java.util.Objects;
public class Person implements Profile, Comparable<Person> {
    private String name;
    private String address;
    private String birthdate;

    public Person(String name, String address, String birthdate){
        this.name=name;
        this.address=address;
        this.birthdate=birthdate;
    }

    @Override
    public  String GetName(){
        return this.name;
    }

    @Override
    public  String GetAddress(){
        return this.address;
    }

    @Override
    public int compareTo(Person o){
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString(){
        return "name: " + this.name + " (Born: " +birthdate + " )";
    }
}