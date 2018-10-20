package entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NamedQuery(name="getAllTariffs", query="from Tariff")
public class Tariff implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true,nullable = false)
    @NotNull @Size(min=3,max=50)
    private String name;

    private boolean isArchived=false;

    private String description;

    @Min(0)
    private int price;

    @ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinTable(
            name = "tariff_tariffoption",
            joinColumns = { @JoinColumn(name = "tariff_id") },
            inverseJoinColumns = { @JoinColumn(name = "option_id") }
    )
    private List<TariffOption> baseOptions =new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinTable(
            name = "tariff_IcompatibleOption",
            joinColumns = { @JoinColumn(name = "tariff_id") },
            inverseJoinColumns = { @JoinColumn(name = "option_id") }
    )
    private Set<TariffOption> incompatibleOptions =new HashSet<>();

    @ManyToMany
    private Set<Tariff> incompatibleTariffs=new HashSet<>();

    public Tariff(){}

    public Tariff(String name){
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<TariffOption> getBaseOptions() {
        return baseOptions;
    }

    public void addBaseOption(TariffOption baseOption) {
            this.baseOptions.add(baseOption);
    }

    public void setBaseOptions(List<TariffOption> options){
        baseOptions.addAll(options);
    }

    public void deleteBaseOption(TariffOption option){
        baseOptions.remove(option);
    }

    public Set<TariffOption> getIncompatibleOptions() {
        return incompatibleOptions;
    }

    public void setIncompatibleOption(TariffOption incompatibleOption) {
            this.incompatibleOptions.add(incompatibleOption);
    }

    public void deleteIncompatibleOption(TariffOption option){
        incompatibleOptions.remove(option);
    }

    public Set<Tariff> getIncompatibleTariffs() {
        return incompatibleTariffs;
    }

    public void setIncompatibleTariffs(Tariff... incompatibleTariffs) {
        for (Tariff tariff:incompatibleTariffs) {
            this.incompatibleTariffs.add(tariff);
        }
    }

    public void deleteIncompatibleTariff(Tariff tariff){
        incompatibleTariffs.remove(tariff);
    }

    public void archive(){
        isArchived=true;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setDescription(String s){
        description=s;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString(){
        return "id: "+id+", tariff: "+name+", price: "+price+", description: "+description+", options: "+baseOptions.toString();
    }
}
