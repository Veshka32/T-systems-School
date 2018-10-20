package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Contract {
    @Id
    private int number;

    @ManyToOne
    private Client owner;

    @ManyToOne
    private Tariff tariff;

    @ManyToMany
    private Set<TariffOption> tariffOptions =new HashSet<>();

    private boolean isBlocked=false;
    private boolean isBlockedByAdmin=false;

    public Contract(){}

    public Contract(int number, Client owner){
        this.number=number;
        this.owner=owner;
    }

    public boolean changeTariff(Tariff tariff){
        this.tariff =tariff;
        return true;
    }


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
