import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuantityMeasurementAppTest {

   //test cases of UC 2

    @Test
    public void testInchEquality_SameValue(){
        Inch inch1 = new Inch(12.0);
        Inch inch2 = new Inch(12.0);

        assertTrue(inch1.equals(inch2));

    }

    @Test
    public void testInchEquality_DifferentValue(){
        Inch inch1 = new Inch(12.0);
        Inch inch2 = new Inch(13.0);

        assertFalse(inch1.equals(inch2));

    }

    @Test
    public void testInchEquality_NullComparison(){
        Inch inch1 = new Inch(12.0);
        Inch inch2 = null;

        assertFalse(inch1.equals(inch2));

    }

    @Test
    public void testInchEquality_DifferentClass(){
        Inch inch1 = new Inch(12.0);
        Double inch2 = 12.0;

        assertFalse(inch1.equals(inch2));

    }

    @Test
    public void testInchEquality_SameReference(){
        Inch inch1 = new Inch(12.0);

        assertTrue(inch1.equals(inch1));

    }


}
