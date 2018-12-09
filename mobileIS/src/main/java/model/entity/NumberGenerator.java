/**
 * This class is used tok keep in database last phone number used {@Contract}
 * and generate next unique phone number by incrementing current one by 1
 */
package model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "numbergenerator")
public class NumberGenerator extends AbstractEntity {

    private long nextNumber;

    /**
     * Get next unique phone number by incrementing current number by 1
     *
     * @return new unique phone number
     */
    public long getNextNumber(){
        return nextNumber++;
    }
}
