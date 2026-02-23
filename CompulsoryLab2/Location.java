public class Location {
    private String name;
    private double locationX;
    private double locationY;

    public Location(String name, double locationX, double locationY){
        this.name=name;
        this.locationX=locationX;
        this.locationY=locationY;
    }

    public String GetName(){
        return this.name;
    }
    public void SetName(String name){
        this.name=name;
    }

    public double GetLocationX(){
        return  this.locationX;
    }
    public void SetLocationX(double locationX){
        this.locationX=locationX;
    }

    public double GetLocationY(){
        return  this.locationY;
    }
    public void SetLocationY(double locationY){
        this.locationY=locationY;
    }

    @Override
    public String toString(){
        return "Location {Name: " + this.name + " Location: " + this.locationX + ", " + this.locationY + "}";
    }
}
