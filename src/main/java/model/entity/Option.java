package model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "option_")
@NamedQueries({
        @NamedQuery(name = "find_by_name", query = "from Option o where o.name=:name"),
        @NamedQuery(name = "is_option_used_in_Contract", query = "select c.id from Contract c join c.options o where o.id=:id"),
        @NamedQuery(name = "is_option_Mandatory", query = "from OptionRelation r where r.another.id=:id"),
        @NamedQuery(name = "is_option_used_in_Tariff", query = "select t.id from Tariff t join t.options o where o.id=:id"),
        @NamedQuery(name = "find_by_names", query = "from Option o where o.name in (:names)"),
        @NamedQuery(name = "get_option_in_tariff_names", query = " select o.name from Tariff t join t.options o where t.id=:id"),
        @NamedQuery(name = "find_by_name_like", query = "from Option o where o.name like :name"),
})
public class Option extends AbstractEntity {

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
        if (!(o instanceof Option)) return false;
        return name.equals(((Option) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
