package model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Table(name = "numbergenerator")
public class NumberGenerator extends AbstractEntity {

    private AtomicLong nextNumber;

    public long getNextNumber(){
        return nextNumber.getAndIncrement();
    }
}
