class Designer extends Person {
    private String favoriteTool;
    public Designer(String name, String address, String birthdate, String tool) {
        super(name, address, birthdate);
        this.favoriteTool = tool;
    }
}