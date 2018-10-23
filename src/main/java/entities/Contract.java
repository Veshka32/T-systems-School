package entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Contract implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @NaturalId
    @GeneratedValue
    private int number;

    @ManyToOne
    private Client owner;

    @ManyToOne
    private Tariff tariff;

    @ManyToMany
    private Set<TariffOption> options =new HashSet<>();

    private boolean isBlocked=false;
    private boolean isBlockedByAdmin=false;

    @Override
    public int hashCode(){
        return number;
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
