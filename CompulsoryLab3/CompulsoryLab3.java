import java.util.*;
public class CompulsoryLab3 {
    public static void main(String[] args) {
        List<Profile> profiles = new ArrayList<>();

        profiles.add(new Person("Alice", "Iasi", "1995-05-12"));
        profiles.add(new Company("Google", "Mountain View", 150000));
        profiles.add(new Person("Charlie", "Bucuresti", "1988-10-20"));
        profiles.add(new Company("Adobe", "San Jose", 26000));

        profiles.sort(Comparator.comparing(Profile::GetName));

        System.out.println("Sorted profiles by name:");
        for (Profile p : profiles) {
            System.out.println(p);
        }

        profiles.sort(Comparator.comparing(Profile::GetName).reversed());
        System.out.println("\nSorted profiles reverse:");
        for (Profile p : profiles) {
            System.out.println(p);
        }
    }
}