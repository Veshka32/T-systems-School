package model.entity;

import javax.persistence.Entity;
import java.util.concurrent.atomic.AtomicLong;

@Entity
public class NumberGenerator extends AbstractEntity {

    private AtomicLong nextNumber;

    public long getNextNumber(){
        return nextNumber.getAndIncrement();
    }
}
