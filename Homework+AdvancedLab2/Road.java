import java.util.Objects;

/**
 * A class that remember the type length, speed limit and the locations from which locations it's going to what location
 */

public class Road {
    private final RoadType type;
    private final double length;
    private final double speedLimit;
    private final Location from;
    private final Location to;

    /**
     *
     * @param type the type of road
     * @param length the length of the road
     * @param speedLimit the speed limit on the road
     * @param from the locations it's starting in
     * @param to the location it's ending in
     */

    public Road(RoadType type, double length, double speedLimit, Location from, Location to){
        double dist=Math.sqrt(Math.pow(from.GetLocationX()-to.GetLocationX(),2)+
                Math.pow(from.GetLocationY()-to.GetLocationY(),2));

        if(length < dist){
            throw new IllegalArgumentException("The length is too short!");
        }

        this.type=type;
        this.length=length;
        this.speedLimit=speedLimit;
        this.from=from;
        this.to=to;
    }
    public RoadType GetType(){
        return this.type;
    }

    public double GetLength(){
        return this.length;
    }

    public double GetSpeedLimit(){
        return this.speedLimit;
    }

    public Location GetFrom(){
        return this.from;
    }

    public Location GetTo(){
        return this.to;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof Road road))
            return false;
        return Double.compare(road.length, length) == 0 &&
                from.equals(road.from) &&
                to.equals(road.to);
    }

    @Override
    public int hashCode(){
        return Objects.hash(length, from, to);
    }

    @Override
    public String toString(){
        return "Road {Type: " + this.type + " Length: " + this.length + " Speed limit: " + this.speedLimit + "}";
    }
}
