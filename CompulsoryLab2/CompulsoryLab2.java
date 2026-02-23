public class CompulsoryLab2{
    public static void main(String[ ] args){
        Location v1 = new Location("Iasi", 47.15, 27.58);
        Location v2 = new Location("Bucuresti", 44.42, 26.10);

        Road r1 = new Road("Autostrada", 389.5, 130);

        System.out.println(v1);
        System.out.println(v2);
        System.out.println(r1);
    }
}