package entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private String name;

    private boolean isArchived=false;

    private String description;

    @Min(0)
    private int price;

    @ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "tariff_option",
            joinColumns = { @JoinColumn(name = "tariff_id") },
            inverseJoinColumns = { @JoinColumn(name = "option_id") }
    )
    @UniqueElements
    private List<TariffOption> options =new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "tariff_IncompatibleOption",
            joinColumns = { @JoinColumn(name = "tariff_id") },
            inverseJoinColumns = { @JoinColumn(name = "option_id") }
    )
    @UniqueElements
    private List<TariffOption> incompatibleOptions =new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @UniqueElements
    @JoinTable(name="tariff_tariff",
            joinColumns={@JoinColumn(name="tariff_id")},
            inverseJoinColumns={@JoinColumn(name="incompatibleTariff_id")})
    private List<Tariff> incompatibleTariffs=new ArrayList<>();

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
