package model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("ALL")
@Entity
@Table(name = "client")
@NamedQueries({
        @NamedQuery(name="is_passport_exists",query = "select c.passportId from Client c where c.passportId=:id"),
        @NamedQuery(name="is_email_exists",query = "select c.email from Client c where c.email=:email"),
        @NamedQuery(name="find_by_passport",query = "from Client c where c.passportId=:passport"),
        @NamedQuery(name = "find_by_email", query = "from Client c where c.email=:email"),
})

public class Client extends AbstractEntity{

    @Column(unique = true)
    private String passportId;

    private String name;

    private String surname;

    private LocalDate birthday;

    private String email;

    private String address;

    @Column
    @OneToMany(mappedBy ="owner",fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Contract> contracts=new HashSet<>();

    public Client() {
        //no arg constructor
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Client)) return false;
        return id == (((Client) o).id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString(){
        return name+" "+surname;
    }

    public String getPassportId() {
        return this.passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        this.contracts = contracts;
    }
}
