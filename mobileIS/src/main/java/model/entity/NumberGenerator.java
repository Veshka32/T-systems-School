package model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "numbergenerator")
public class NumberGenerator extends AbstractEntity {

    private long nextNumber;

    public long getNextNumber(){
        return nextNumber++;
    }
}
