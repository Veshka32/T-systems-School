package model.entity;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(name = "get_options",query = "select t.options from Tariff t where t.id=:id"),
        @NamedQuery(name = "find_tariff_by_name", query = "from Tariff o where o.name=:name"),
        @NamedQuery(name = "is_used", query = "select c.id from Contract c where c.tariff.id=:id"),
})
public class Tariff extends AbstractEntity{

    @Expose
    @Column(unique = true,nullable = false)
    private String name;

    @Expose
    private String description;

    @Expose
    private BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "tariff_option",
            joinColumns = { @JoinColumn(name = "tariff_id") },
            inverseJoinColumns = { @JoinColumn(name = "option_id") }
    )
    private Set<Option> options = new HashSet<>();

    public Tariff(String name){
        this.name=name;
    }

    public Tariff() {
        //no arg constructor
    }

    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public String toString(){
        return name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<Option> getOptions() {
        return this.options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }
}
