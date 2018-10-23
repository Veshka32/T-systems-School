package entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NamedQuery(name="getAllTariffs", query="from Tariff")
public class Tariff implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true,nullable = false)
    @NotNull @Size(min=3,max=50)
    @NaturalId
    private String name;

    private boolean isArchived=false;

    private String description;

    @Min(0)
    private int price;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "tariff_option",
            joinColumns = { @JoinColumn(name = "tariff_id") },
            inverseJoinColumns = { @JoinColumn(name = "option_id") }
    )
    private Set<TariffOption> options =new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "tariff_IncompatibleOption",
            joinColumns = { @JoinColumn(name = "tariff_id") },
            inverseJoinColumns = { @JoinColumn(name = "option_id") }
    )
    private Set<TariffOption> incompatibleOptions =new HashSet<>();

    public Tariff(){}

    public Tariff(String name){
        this.name=name;
    }

    public void deleteOption(TariffOption option){
        options.remove(option);
    }

    public void deleteIncompatibleOption(TariffOption option){
        incompatibleOptions.remove(option);
    }

    @Override
    public String toString(){
        return "id: "+id+", tariff: "+name+", price: "+price+", description: "+description+", is archived:"+isArchived;
    }
}
