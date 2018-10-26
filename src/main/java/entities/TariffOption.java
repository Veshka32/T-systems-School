package entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name = "is_name_exists",query = "select o.name from TariffOption o where o.name=:name"),
        @NamedQuery(name="find_by_name",query = "from TariffOption o where o.name=:name")
})
public class TariffOption extends AbstractEntity{

    @NotBlank(message = "option.name.invalid")
   @Size(min = 3,max = 50,message = "option.name.invalid")
    @Column(unique = true)
    private String name;

    @Min(value = 0,message = "option.price.invalid")
    private int price;

    @Min(value = 0,message = "option.subscribeCost.invalid")
    private int subscribeCost;

    private boolean archived;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @UniqueElements
    @JoinTable(name="option_IncompatibleOption",
            joinColumns={@JoinColumn(name="option_id")},
            inverseJoinColumns={@JoinColumn(name="incompatibleOption_id")})
    private Set<TariffOption> incompatibleOptions=new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @UniqueElements
    @JoinTable(name="option_MandatoryOption",
            joinColumns={@JoinColumn(name="option_id")},
            inverseJoinColumns={@JoinColumn(name="mandatoryOption_id")})
    private Set<TariffOption> mandatoryOptions=new HashSet<>();

    public void addIncompatibleOption(TariffOption option){
        incompatibleOptions.add(option);
    }

    public void removeIncompatibleOption(TariffOption option){
        incompatibleOptions.remove(option);
    }

    public void addMandatoryOption(TariffOption option){
        mandatoryOptions.add(option);
    }

    public void removeMandatoryOption(TariffOption option){
        mandatoryOptions.remove(option);
    }

    @Override
    public String toString(){
        return name+": "+description;
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
}
