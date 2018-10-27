package entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NamedQueries({
        @NamedQuery(name = "is_tariffName_exists", query = "select o.name from Tariff o where o.name=:name"),
        @NamedQuery(name = "get_options",query = "select t.options from Tariff t where t.id=:id"),
        @NamedQuery(name = "find_tariff_by_name", query = "from Tariff o where o.name=:name"),
        @NamedQuery(name="is_used",query = "select c.id from Contract c where c.tariff.id=:id")
})
public class Tariff extends AbstractEntity{

    @Column(unique = true,nullable = false)
    @NotBlank(message = "{tariff.name.invalid}")
    @Size(min=3,max=50,message = "{tariff.name.invalid}")
    private String name;

    private boolean isArchived=false;

    private String description;

    @Min(value = 0,message = "tariff.price.invalid")
    private int price;

    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "tariff_option",
            joinColumns = { @JoinColumn(name = "tariff_id") },
            inverseJoinColumns = { @JoinColumn(name = "option_id") }
    )
    private Set<TariffOption> options =new HashSet<>();

    public Tariff(){}

    public Tariff(String name){
        this.name=name;
    }

    public void deleteOption(TariffOption option){
        options.remove(option);
    }

    public void addOption(TariffOption option){
        options.add(option);
    }

    @Override
    public String toString(){
        return "id: "+id+", tariff: "+name+", price: "+price+", description: "+description+", is archived:"+isArchived;
    }
}
