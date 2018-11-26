package model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "option_")
@NamedQueries({
        @NamedQuery(name = "find_by_name", query = "from Option o where o.name=:name"),
        @NamedQuery(name = "is_option_used_in_Contract", query = "select c.id from Contract c join c.options o where o.id=:id"),
        @NamedQuery(name = "is_option_Mandatory", query = "from OptionRelation r where r.another.id=:id"),
        @NamedQuery(name = "is_option_used_in_Tariff", query = "select t.id from Tariff t join t.options o where o.id=:id"),
        @NamedQuery(name = "find_by_ids", query = "from Option o where o.id in (:ids)"),
        @NamedQuery(name = "find_by_name_like", query = "from Option o where o.name like :name"),
})
public class Option extends AbstractEntity {

    @Column(unique = true)
    private String name;

    private BigDecimal price;

    private BigDecimal subscribeCost;

    private String description;

    public Option() {
        //no arg constructor
    }

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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSubscribeCost() {
        return this.subscribeCost;
    }

    public void setSubscribeCost(BigDecimal subscribeCost) {
        this.subscribeCost = subscribeCost;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
