package entities;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
public class TariffOption implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @NotEmpty
    @Column(unique = true)
    private String name;

    @Min(0)
    private int price;//make > 0

    @Min(0)
    private int subscribeCost;

    private String description;
    private boolean archived;

    @ManyToMany
    private Set<TariffOption> incompatibleOptions=new HashSet<>();

    public TariffOption(){}

    public TariffOption(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return "TariffOption: "+name+", price: "+price+", cost of subscribe: "+subscribeCost;
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

    public int getSubscriveCost() {
        return subscribeCost;
    }

    public void setSubscriveCost(int subscriveCost) {
        this.subscribeCost = subscriveCost;
    }

    public Set<TariffOption> getIncompatibleTariffOptions() {
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
