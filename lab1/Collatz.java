/** Class that prints the Collatz sequence starting from a given number.
 *  @author LIMINGNING
 */
public class Collatz {

    /** Real implementation of nextNumber! */
    public static int nextNumber(int n) {
        if (n % 2 == 0)
            n = n / 2;
        else
            n = 3*n + 1;

        return n;
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

