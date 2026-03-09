public class CompulsoryLab3 {
    public static void main(String[] args) {
        SocialNetwork network = new SocialNetwork();

        Programmer p1 = new Programmer("Alice", "Iasi", "1995-05-12", "Java");
        Programmer p2 = new Programmer("Bob", "Cluj", "1990-01-20", "Python");
        Designer d1 = new Designer("Charlie", "Bucuresti", "1998-11-05", "Figma");
        Company c1 = new Company("Google", "Mountain View", 150000);
        Company c2 = new Company("Adobe", "San Jose", 26000);

        p1.addRelationship(p2, "friend");
        p1.addRelationship(c1, "employer");
        p1.addRelationship(d1, "colleague");

        p2.addRelationship(p1, "friend");
        p2.addRelationship(c1, "employer");

        c1.addRelationship(p1, "employee");
        c1.addRelationship(p2, "employee");

        c2.addRelationship(d1, "partner");

        d1.addRelationship(p1, "colleague");

        network.addProfile(p1);
        network.addProfile(p2);
        network.addProfile(d1);
        network.addProfile(c1);
        network.addProfile(c2);

        network.printNetworkByImportance();


    }
}