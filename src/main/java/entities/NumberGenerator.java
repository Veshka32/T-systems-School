package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

@Entity
public class NumberGenerator implements Serializable {

    @Id
    private int id;

    private Long nextNumber;

    public long getNextNumber(){
        return nextNumber++;
    }
}
