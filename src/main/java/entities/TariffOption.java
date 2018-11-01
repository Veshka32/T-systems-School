package entities;

import lombok.Getter;
import lombok.Setter;

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
        @NamedQuery(name = "is_name_exists", query = "select o.name from TariffOption o where o.name=:name"),
        @NamedQuery(name = "find_by_name", query = "from TariffOption o where o.name=:name"),
        @NamedQuery(name = "is_option_used_in_Contract", query = "select c.id from Contract c join c.options o where o.id=:id"),
        @NamedQuery(name = "is_option_Mandatory", query = "select o.id from TariffOption o join o.mandatoryOptions m where m.id=:id"),
        @NamedQuery(name = "is_option_used_in_Tariff", query = "select t.id from Tariff t join t.options o where o.id=:id"),
        @NamedQuery(name = "get_options_in_range", query = "from TariffOption o where o.id in (:ids)"),
        @NamedQuery(name="get_all_mandatory",query = "select m.name from TariffOption o join o.mandatoryOptions m where o.name in (:names)"),
        @NamedQuery(name="get_all_incompatible",query = "select i.name from TariffOption o join o.incompatibleOptions i where o.name in (:names)")
})
public class TariffOption extends AbstractEntity {

    @Column(unique = true)
    private String name;

    private int price;

    private int subscribeCost;

    private String description;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "incompatible_options",
            joinColumns = {@JoinColumn(name = "option_id")},
            inverseJoinColumns = {@JoinColumn(name = "incompatible_id")})
    private Set<TariffOption> incompatibleOptions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "mandatory_options",
            joinColumns = {@JoinColumn(name = "option_id")},
            inverseJoinColumns = {@JoinColumn(name = "mandatory_id")})
    private Set<TariffOption> mandatoryOptions = new HashSet<>();

    public void addIncompatibleOption(TariffOption option) {
        incompatibleOptions.add(option);
    }

    public void removeIncompatibleOption(TariffOption option) {
        incompatibleOptions.remove(option);
    }

    public void addMandatoryOption(TariffOption option) {
        mandatoryOptions.add(option);
    }

    @Override
    public String toString() {
        return name + ": " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TariffOption)) return false;
        return name.equals(((TariffOption) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
