package application;

public enum RANK{
    GOLD_STRIPE(0),
    GOLD_BELT(1);
    //GREEN_STRIPE(2),
    //GREEN_BELT(3),
    //PURPLE_STRIPE(4),
    //PURPLE_BELT(5),
    //BROWN_STRIPE(6),
    //BROWN_BELT(7),
    //RED_STRIPE(8),
    //RED_BELT(9),
    //FIRST_DEGREE(10);


        //ranks.add("1st of 2nd");
        //ranks.add("2nd Degree");
        //ranks.add("1st of 3rd");
        //ranks.add("2nd of 3rd");
        //ranks.add("3rd Degree");
        //ranks.add("1st of 4th");
        //ranks.add("2nd of 4th");
        //ranks.add("3rd of 4th");
        //ranks.add("4th Degree");
        //ranks.add("1st of 5th");
        //ranks.add("2nd of 5th");
        //ranks.add("3rd of 5th");
        //ranks.add("4th of 5th");
        //ranks.add("5th Degree");

    private final int value;

    RANK(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
