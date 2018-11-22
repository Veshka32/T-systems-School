package model.entity;

import javax.persistence.Entity;

@Entity
public class NumberGenerator extends AbstractEntity {

    private Long nextNumber;

    public long getNextNumber(){
        return nextNumber++;
    }
}
