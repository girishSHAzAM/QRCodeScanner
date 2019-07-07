package com.example.qrcodescanner;

public class ClassOrganiser {
    private String name,orderedFood;

    public ClassOrganiser(String name,String orderedFood){
        this.name=name;
        this.orderedFood=orderedFood;
    }

    public String getName() {
        return name;
    }

    public String getOrderedFood() {
        return orderedFood;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrderedFood(String orderedFood) {
        this.orderedFood = orderedFood;
    }
}
