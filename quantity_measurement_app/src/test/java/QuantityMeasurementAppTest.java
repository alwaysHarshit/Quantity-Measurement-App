import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuantityMeasurementAppTest {

    @Test
    public void testFeetEquality_SameValue(){
        Feet feet1 = new Feet(12.0);
        Feet feet2 = new Feet(12.0);

        assertTrue(feet1.equals(feet2));

    }

    @Test
    public void testFeetEquality_DifferentValue(){
        Feet feet1 = new Feet(12.0);
        Feet feet2 = new Feet(13.0);

        assertFalse(feet1.equals(feet2));

    }

    @Test
    public void testFeetEquality_NullComparison(){
        Feet feet1 = new Feet(12.0);
        Feet feet2 = null;

        assertFalse(feet1.equals(feet2));

    }

    @Test
    public void testFeetEquality_DifferentClass(){
        Feet feet1 = new Feet(12.0);
        Double feet2 = 12.0;

        assertFalse(feet1.equals(feet2));

    }

    @Test
    public void testFeetEquality_SameReference(){
        Feet feet1 = new Feet(12.0);

        assertTrue(feet1.equals(feet1));

    }

}
