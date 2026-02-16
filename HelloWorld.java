public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        String[] languages = {"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};
        int n = (int) (Math.random() * 1_000_000);
        n = n * 3;
        n = n + 0b10101;
        n = n + 0xFF;
        n = n * 6;
        while(n>9)
        {
            int s = 0;
            while(n!=0)
            {
                s = s + n%10;
                n = n/10;
            }
            n = s;
        }
        System.out.println("Willy-nilly, this semester I will learn " + languages[n]);
    }

}