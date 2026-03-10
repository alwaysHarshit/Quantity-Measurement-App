enum LengthUnit {

    //every unit is convert into in inch so the converstions facotrs convert these units into inchs
    Feet(12.0),
    Inch(1.0),
    yards (36.0),
    Cm(0.393701);

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
        double v1=1.0;
        double v2=36.0;

        QuantityLength q1= new QuantityLength(v1,LengthUnit.yards);
        QuantityLength q2=new QuantityLength(v2,LengthUnit.Inch);
//        boolean equals = q1.equals(q2);
//        System.out.printf("Are lengths both lenghts %f in Yard and %f in Inch are equal? %b    ",v1,v2,equals);
        double v3=100;
        double v4=39.3701;
        QuantityLength q3=new QuantityLength(v3,LengthUnit.Cm);
        QuantityLength q4=new QuantityLength(v4,LengthUnit.Inch);
        boolean equals = q3.equals(q4);
        System.out.printf("Are lengths both lenghts %f in CM and %f in Inch are equal? %b    ",v3,v4,equals);}


}
