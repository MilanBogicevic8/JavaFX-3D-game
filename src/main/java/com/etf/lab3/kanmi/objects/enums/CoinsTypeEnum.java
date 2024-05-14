package com.etf.lab3.kanmi.objects.enums;

public enum CoinsTypeEnum {
    BLUE(5),
    YELLOW(1),
    GREEN(3);

    private final Integer num_of_points;

    public int getPoints(){
        return num_of_points;
    }
    CoinsTypeEnum(Integer num){
        this.num_of_points=num;
    }
}
