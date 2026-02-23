public class Road {
    private  String type;
    private double length;
    private double speedLimit;

    public Road(String type, double length, double speedLimit){
        this.type=type;
        this.length=length;
        this.speedLimit=speedLimit;
    }
    public String GetType(){
        return this.type;
    }
    public void SetType(String type){
        this.type=type;
    }

    public double GetLength(){
        return this.length;
    }
    public void SetLength(double length){
        this.length=length;
    }

    public double GetSpeedLimit(){
        return this.speedLimit;
    }
    public void SetSpeedLimit(double speedLimit){
        this.speedLimit=speedLimit;
    }

    @Override
    public String toString(){
        return "Road {Type: " + this.type + " Length: " + this.length + " Speed limit: " + this.speedLimit + "}";
    }
}
