enum LengthUnit {
    Feet(12.0),
    Inch(1.0);

    private final double converstionFactor;

    LengthUnit( double converstionFactor){
        this.converstionFactor = converstionFactor;
    }

    public double getConverstionFactor() {
        return converstionFactor;
    }

}
public class QuantityLength {

    private  final double value;
    private LengthUnit unit;



    public QuantityLength(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object obj) {
        //agr done oject same memory location ko refer kare to
        if(this==obj) return true;

        if(obj==null || obj.getClass()!=this.getClass()) return false;

        QuantityLength thatLength=(QuantityLength) obj;
        return compare(thatLength);


    }

    private boolean compare(QuantityLength thatLength) {
        return this.convertToBaseUnit()== thatLength.convertToBaseUnit();
    }

    private double convertToBaseUnit() {
        return value*unit.getConverstionFactor();
    }

    //method to compare the the the qunatity objects


    public static void main(String[] args){
        double v1=12.0;
        double v2=12.0;

        QuantityLength q1= new QuantityLength(v1,LengthUnit.Inch);
        QuantityLength q2=new QuantityLength(v2,LengthUnit.Feet);
        boolean equals = q1.equals(q2);
        System.out.printf("Are lengths both lenghts %f in inchs and %f in feet are equal? %b    ",v1,v2,equals);

    }


}
