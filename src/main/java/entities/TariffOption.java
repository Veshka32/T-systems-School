package entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.Objects;


@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name = "find_by_name", query = "from TariffOption o where o.name=:name"),
        @NamedQuery(name = "is_option_used_in_Contract", query = "select c.id from Contract c join c.options o where o.id=:id"),
        @NamedQuery(name = "is_option_Mandatory", query = "from OptionRelation r where r.another.id=:id"),
        @NamedQuery(name = "is_option_used_in_Tariff", query = "select t.id from Tariff t join t.options o where o.id=:id"),
        @NamedQuery(name="find_by_names",query = "from TariffOption o where o.name in (:names)"),
})
public class TariffOption extends AbstractEntity {

    @Column(unique = true)
    private String name;

    private int price;

    private int subscribeCost;

    private String description;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinTable(name="optionrelation",
//    joinColumns = {@JoinColumn(name="one_id")},
//    inverseJoinColumns = {@JoinColumn(name = "another_id")})
//    Set<OptionRelation> relations;

////    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
////    @JoinTable(name = "incompatible_options",
////            joinColumns = {@JoinColumn(name = "option_id")},
////            inverseJoinColumns = {@JoinColumn(name = "incompatible_id")})
//    private transient Set<TariffOption> incompatibleOptions = new HashSet<>();
//
////    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
////    @JoinTable(name = "mandatory_options",
////            joinColumns = {@JoinColumn(name = "option_id")},
////            inverseJoinColumns = {@JoinColumn(name = "mandatory_id")})
//    private transient Set<TariffOption> mandatoryOptions = new HashSet<>();

//    public void addIncompatibleOption(TariffOption option) {
//        incompatibleOptions.add(option);
//    }
//
//    public void removeIncompatibleOption(TariffOption option) {
//        incompatibleOptions.remove(option);
//    }
//
//    public void addMandatoryOption(TariffOption option) {
//        mandatoryOptions.add(option);
//    }

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
