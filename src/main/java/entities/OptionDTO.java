package entities;

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class OptionDTO {

    private String name;
    private int price;
    private int subscribeCost;
    private boolean archived;
    private String description;

    private Set<TariffOption> incompatibleOptions=new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @UniqueElements
    @JoinTable(name="option_MandatoryOption",
            joinColumns={@JoinColumn(name="option_id")},
            inverseJoinColumns={@JoinColumn(name="mandatoryOption_id")})
    private Set<TariffOption> mandatoryOptions=new HashSet<>();
}
