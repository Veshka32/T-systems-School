package entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
public class Client implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    @Digits(integer = 10,fraction = 0)
    private int passportId;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    private LocalDate birthday;

    @Column(nullable = false)
    @Email
    private String email;

    private String address;

    @Column
    @OneToMany(mappedBy ="owner")
    @JoinTable(
            name = "client_contract",
            joinColumns = { @JoinColumn(name = "client_id") },
            inverseJoinColumns = { @JoinColumn(name = "contract_id") }
    )
    private Set<Contract> contracts=new HashSet<>();

    @Override
    public String toString(){
        return name+" "+surname+", number "+contracts.stream().map(Contract::getNumber).collect(Collectors.toList());
    }
}
