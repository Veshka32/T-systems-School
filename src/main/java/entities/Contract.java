package entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Contract implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @NaturalId
    @Range(min = 1000000000L,max=9999999999L)
    private long number;

    @ManyToOne
    private Client owner;

    @ManyToOne
    private Tariff tariff;

    @ManyToMany()
    private Set<TariffOption> options =new HashSet<>();

    private boolean isBlocked=false;
    private boolean isBlockedByAdmin=false;

    public Contract(long phone,Client client){
        owner=client;number=phone;
    }
    @Override
    public int hashCode(){
        return (int)number;
    }

    @Override
    public boolean equals(Object o){
        if ( !(o instanceof Contract)) return false;
        return number==((Contract)o).number;
    }

    @Override
    public String toString(){
        return "number: "+number+", tariff:"+tariff.toString()+", owner: "+owner.toString();
    }
}
