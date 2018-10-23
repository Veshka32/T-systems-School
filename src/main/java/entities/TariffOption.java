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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Getter
@Setter
@Entity
public class TariffOption implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank @Size(min = 3,max = 50)
    @NaturalId
    @Column(unique = true)
    private String name;

    @Min(0)
    private int price;

    @Min(0)
    private int subscribeCost;

    private boolean archived;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @UniqueElements
    @JoinTable(name="option_option",
            joinColumns={@JoinColumn(name="option_id")},
            inverseJoinColumns={@JoinColumn(name="incompatibleOption_id")})
    private List<TariffOption> incompatibleOptions=new ArrayList<>();

    public void deleteIncompatibleOption(TariffOption option){
        incompatibleOptions.remove(option);
    }

    public void addIncompatibleOption(TariffOption option){
        incompatibleOptions.add(option);
    }

    @Override
    public String toString(){
        return name+": price: "+price+", cost of subscribe: "+subscribeCost+", is archived: "+archived+", incompatible options: "+incompatibleOptions.stream().map(TariffOption::getName).collect(Collectors.toList());
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
