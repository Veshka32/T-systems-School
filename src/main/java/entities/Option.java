package entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.math.BigDecimal;
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
        @NamedQuery(name = "get_option_in_tariff_names", query = "select o.name from Tariff t join Tariff.options o where t.id=:id"),
})
public class TariffOption extends AbstractEntity {

    @Column(unique = true)
    private String name;

    private BigDecimal price;

    private BigDecimal subscribeCost;

    private String description;

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
