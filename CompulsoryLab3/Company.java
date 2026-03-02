public class Company implements Profile, Comparable<Company>{
    private String name;
    private String address;
    private int employees;

    public Company(String name, String address, int employees){
        this.name=name;
        this.address=address;
        this.employees=employees;
    }

    @Override
    public String GetName(){
        return this.name;
    }

    @Override
    public String GetAddress(){
        return this.address;
    }

    @Override
    public int compareTo(Company o){
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString(){
        return "name: " + this.name + " (Number of emplyees: " +employees + " )";
    }
}
