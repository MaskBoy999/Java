class Programmer extends Person {
    private String favoriteLanguage;
    public Programmer(String name, String address, String birthdate, String lang) {
        super(name, address, birthdate);
        this.favoriteLanguage = lang;
    }
}