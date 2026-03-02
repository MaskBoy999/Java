final class City extends Location{
    private final int population;
    public City(String name, double x, double y, int pop)
    {
        super(name, x, y);
        this.population = pop;
    }
    public int getPopulation()
    {
        return population;
    }
}