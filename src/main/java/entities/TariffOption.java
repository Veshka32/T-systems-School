package entities;

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.*;


@Entity
public class TariffOption implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @NotEmpty
    @NaturalId
    @Column(unique = true)
    private String name;

    @Min(0)
    private int price;//make > 0

    @Min(0)
    private int subscribeCost;

    private boolean archived;

    private String description;

    @ManyToMany
    @UniqueElements
    private List<TariffOption> incompatibleOptions=new ArrayList<>();

    public TariffOption(){}

    public TariffOption(String name,int price){
        this.name=name; this.price=price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return name+": price: "+price+", cost of subscribe: "+subscribeCost;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof TariffOption)) return false;
        return name.equals(((TariffOption)o).name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSubscribeCost() {
        return subscribeCost;
    }

    public void setSubscriveCost(int subscriveCost) {
        this.subscribeCost = subscriveCost;
    }

    public List<TariffOption> getIncompatibleTariffOptions() {
        return incompatibleOptions;
    }

    public void addIncompatibleOptions(TariffOption option) {
        incompatibleOptions.add(option);
    }

    public void removeIncompatibleOptions(TariffOption option) {
        incompatibleOptions.remove(option);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setArchived(){
        archived=true;
    }
}
