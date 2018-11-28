package com;

import java.io.Serializable;
import java.math.BigDecimal;

//must be serializable because is used in @applicationScoped bean
public class Tariff implements Serializable {

    private String name;

    private String description;

    private BigDecimal price;

    public Tariff() {
        //no arg constructor
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
