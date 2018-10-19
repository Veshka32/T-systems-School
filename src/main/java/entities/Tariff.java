package entities;

import jdk.nashorn.internal.objects.annotations.Getter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty
    private String name;

    boolean isArchived=false;

    private String description;

    @Min(0)
    private int price;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<TariffOption> baseOptions =new ArrayList<>();

    @ManyToMany
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
            description=baseOptions.stream().map(TariffOption::getName).collect(Collectors.joining(","));
    }

    public void setBaseOptions(List<TariffOption> options){
        baseOptions.addAll(options);
    }

    public void deleteBaseOption(TariffOption option){
        baseOptions.remove(option);
        description=baseOptions.stream().map(TariffOption::getName).collect(Collectors.joining(","));
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

    @Override
    public String toString(){
        return "id: "+id+", tariff: "+name+", price: "+price+", base options: "+description;
    }
}
