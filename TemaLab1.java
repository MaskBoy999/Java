import java.util.Arrays;

public class TemaLab1{
    public static void main(String[] args)
    {
        int n=Integer.parseInt(args[0]);
        String type=args[1].toLowerCase();
        long startTime=0;
        long endTime=0;
        long duration=0;
        try
        {
            startTime=System.nanoTime();

            int[][] matrix=new int[n][n];

            if(type.equals("rectangle")){
                CreateRectangle(matrix, n);
            } else if (type.equals("circle")) {
                CreateCircle(matrix, n);
            }
            endTime=System.nanoTime();
            duration=endTime-startTime;
            if (n <= 50) {
                System.out.println(matrixToString(matrix));
            } else {
                System.out.println("Imaginea este prea mare pentru afisare text");
                System.out.println("Timp de executie: " + (duration / 1_000_000.0) + " ms");
            }
        }
        catch (OutOfMemoryError e)
        {
            System.err.println("Memorie insuficienta pentru n=" + n);
        }
    }
    private static void CreateRectangle(int[][] m, int n)
    {
        for(int i=0; i<n; i++) Arrays.fill(m[i], 255);

        int margine=n/6;
        for (int i=margine; i<n-margine; i++){
            for (int j=margine; j<n-margine; j++){
                m[i][j]=150;
            }
        }
        for (int i=2*margine; i<n-2*margine; i++){
            for (int j=2*margine; j<n-2*margine; j++){
                m[i][j]=0;
            }
        }
    }

    private static void CreateCircle(int[][] m, int n)
    {
        for(int i=0; i<n; i++) Arrays.fill(m[i], 0);
        int centerX=n/2;
        int centerY=n/2;
        int radius=n/3;
        for (int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                double dist = Math.sqrt (Math.pow (i-centerX, 2) + Math.pow (j-centerY, 2));
                m[i][j]=(dist <= radius) ? ((dist <= radius/2) ? 255 : 150) : 0;
            }
        }
    }
    private static String matrixToString(int[][] m)
    {
        StringBuilder sb = new StringBuilder();
        for (int[] row : m) {
            for (int val : row) {
                if (val > 200) sb.append("██");
                else if (val > 100) sb.append("▒▒");
                else sb.append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}