package entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
@Entity
public class TariffOption extends AbstractEntity{

    @NotBlank @Size(min = 3,max = 50,message = "option.name.invalid")
    @NaturalId
    @Column(unique = true)
    private String name;

    @Min(value = 0,message = "option.price.invalid")
    private int price;

    @Min(value = 0,message = "option.price.invalid")
    private int subscribeCost;

    private boolean archived;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @UniqueElements
    @JoinTable(name="option_option",
            joinColumns={@JoinColumn(name="option_id")},
            inverseJoinColumns={@JoinColumn(name="incompatibleOption_id")})
    private Set<TariffOption> incompatibleOptions=new HashSet<>();

    public void addIncompatibleOption(TariffOption option){
        incompatibleOptions.add(option);
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
