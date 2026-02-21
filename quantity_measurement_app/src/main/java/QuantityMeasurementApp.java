import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Feet{
    private final  double value;

    public Feet(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || obj.getClass() !=this.getClass()) return false;

        return Double.compare(((Feet) obj).value, this.value)==0;
    }
}

class Inch{
    private  final  double value;

    public Inch(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {

        //agr inch ka object null hai ya phir wo es class se match nhi kar rha to false return kardo
        if(obj==null || obj.getClass()!=this.getClass()) return false;

        //compare the value of currett object value with another objects value
        //0-> mean values are equal
        // -ve -> this.value> obj.value
        // +ve -> this.value< obj.value
        return Double.compare(this.value, ((Inch) obj).value)==0;
    }
}

public class QuantityMeasurementApp  {
    private static double readDouble(BufferedReader reader, String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String input = reader.readLine();

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Please try again.");
            }
        }
    }
    public static void main(String[] args)  {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            double val1 = readDouble(bufferedReader, "Enter value 1 in feet: ");
            double val2 = readDouble(bufferedReader, "Enter value 2 in feet: ");

            Feet f1 = new Feet(val1);
            Feet f2 = new Feet(val2);

            boolean equals = f1.equals(f2);
            System.out.printf("The values are equal: %b%n", equals);

        } catch (IOException e) {
            System.out.println("❌ Input error: " + e.getMessage());
        }

        try {
            double val1 = readDouble(bufferedReader, "Enter value 1 in inch: ");
            double val2 = readDouble(bufferedReader, "Enter value 2 in inch: ");

            Inch f1 = new Inch(val1);
            Inch f2 = new Inch(val2);

            boolean equals = f1.equals(f2);
            System.out.printf("The values are equal: %b%n", equals);
        } catch (IOException e) {
            System.out.println("❌ Input error: " + e.getMessage());
        }


    }

}
