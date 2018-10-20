package entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    @Id //reg ex?
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private int passportId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    private byte[] password;
    @Column(length = 8)
    private byte[] salt;

    @Column(nullable = false)
    @OneToMany(mappedBy ="owner")
    private Set<Contract> contracts=new HashSet<>(); //not empty?

    public Client(){}

    public Client(String name, String surname, LocalDate birthday, String email, Contract contract){
        this.name=name;
        this.surname=surname;
        this.birthday=birthday;
        this.email=email;
        contracts.add(contract);
    }

    @Override
    public String toString(){
        return name+" "+surname+", number "+contracts.toString();
    }

    public int getPassportId() {
        return passportId;
    }

    public void setPassportId(int passportId) {
        this.passportId = passportId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public Set<Contract> getContracts() {
        return contracts;
    }

    public void addContracts(Contract contract) {
        contracts.add(contract);
    }
}
