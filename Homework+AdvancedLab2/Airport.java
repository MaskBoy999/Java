final class Airport extends Location{
    private final int terminals;
    public Airport(String name, double x, double y, int t)
    {
        super(name, x, y); this.terminals = t;
    }
    public int getTerminals()
    {
        return terminals;
    }
}