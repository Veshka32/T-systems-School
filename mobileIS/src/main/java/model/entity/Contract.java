package model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contract")
@NamedQueries({
        @NamedQuery(name = "get_client_contracts", query = "from Contract c where c.owner.id=:clientId"),
        @NamedQuery(name = "get_client_by_phone", query = "select c.owner from Contract c where c.number=:phone"),
        @NamedQuery(name = "get_contractId_by_phone", query = "select c.id from Contract c where c.number=:phone"),
        @NamedQuery(name = "find_by_phone", query = "from Contract c where c.number=:phone"),
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
    @JoinTable(name = "contract_option",
            joinColumns = {@JoinColumn(name = "contract_id")},
            inverseJoinColumns = {@JoinColumn(name = "option_id")})
    private Set<Option> options = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "contract")
    private User user;

    private boolean isBlocked = false;
    private boolean isBlockedByAdmin = false;

    public Contract(long phone, Client client) {
        owner = client;
        number = phone;
    }

    public Contract() {
        //no arg constructor
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

    public long getNumber() {
        return this.number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Client getOwner() {
        return this.owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public Tariff getTariff() {
        return this.tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public Set<Option> getOptions() {
        return this.options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isBlocked() {
        return this.isBlocked;
    }

    public void setBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public boolean isBlockedByAdmin() {
        return this.isBlockedByAdmin;
    }

    public void setBlockedByAdmin(boolean isBlockedByAdmin) {
        this.isBlockedByAdmin = isBlockedByAdmin;
    }
}
