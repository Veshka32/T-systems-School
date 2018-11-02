package entities.helpers;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NumberGenerator{

    @Id
    private int id;

    private Long nextNumber;

    public long getNextNumber(){
        return nextNumber++;
    }
}
