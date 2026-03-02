import java.util.Objects;

/**
 * Abstract sealed class for different type of locations
 * Base for different types of locations
 * * @author Dorcu Eduard-Daniel
 * @version 1.0
 */

public abstract sealed class Location permits City, Airport, GasStation{
    protected String name;
    protected double locationX;
    protected double locationY;
    /**
     * Constructoir for location
     * * @param name the name of the location
     * @param locationX The coordinate X on the map
     * @param locationY The coordinate Y on the map
     */
    public Location(String name, double locationX, double locationY){
        this.name=name;
        this.locationX=locationX;
        this.locationY=locationY;
    }

    public String GetName(){
        return this.name;
    }

    public double GetLocationX(){
        return  this.locationX;
    }

    public double GetLocationY(){
        return  this.locationY;
    }

    @Override
    public String toString(){
        return "Location {Name: " + this.name + " Location: " + this.locationX + ", " + this.locationY + "}";
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Location location))
            return false;
        return Double.compare(location.locationX, locationX) == 0 &&
                Double.compare(location.locationY, locationY) == 0 &&
                name.equals(location.name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name, locationX, locationY);
    }
}
