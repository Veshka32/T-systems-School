package model.entity;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "get_options",query = "select t.options from Tariff t where t.id=:id"),
        @NamedQuery(name = "find_tariff_by_name", query = "from Tariff o where o.name=:name"),
        @NamedQuery(name="is_used",query = "select c.id from Contract c where c.tariff.id=:id")
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

    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public String toString(){
        return name;
    }
}