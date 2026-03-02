final class GasStation extends Location{
    private final double gasPrice;
    public GasStation(String name, double x, double y, double price)
    {
        super(name, x, y); this.gasPrice = price;
    }
    public double getGasPrice()
    {
        return gasPrice;
    }
}