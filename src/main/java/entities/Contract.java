package entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(name = "get_client_contracts", query = "from Contract c where c.owner.id=:clientId"),
        @NamedQuery(name = "get_client_by_phone", query = "select c.owner from Contract c where c.number=:phone"),
        @NamedQuery(name = "get_contract_by_phone",query = "from Contract c where c.number=:phone")
})

public class Contract extends AbstractEntity {

    @Column(updatable = false)
    private long number;

    @ManyToOne
    @JoinColumn(updatable = false)
    private Client owner;

    @ManyToOne
    private Tariff tariff;

    @ManyToMany
    @JoinTable(name = "contract_tariffoption",
            joinColumns = {@JoinColumn(name = "Contract_id")},
            inverseJoinColumns = {@JoinColumn(name = "options_id")})
    private Set<TariffOption> options = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private MyUser user;

    private boolean isBlocked = false;
    private boolean isBlockedByAdmin = false;

    public Contract(long phone, Client client) {
        owner = client;
        number = phone;
    }

    @Override
    public int hashCode() {
        return (int) number;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Contract)) return false;
        return number == ((Contract) o).number;
    }

    @Override
    public String toString() {
        return "number: " + number + ", tariff:" + tariff.toString() + ", owner: " + owner.toString();
    }
}
