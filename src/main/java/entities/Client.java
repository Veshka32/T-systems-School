package entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

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
@NamedQueries({
        @NamedQuery(name="is_passport_exists",query = "select c.passportId from Client c where c.passportId=:id"),
        @NamedQuery(name="is_email_exists",query = "select c.email from Client c where c.email=:email")
})

public class Client extends AbstractEntity{

    @Column(unique = true)
    private String passportId;

    private String name;

    private String surname;

//    @Past
//    @DateTimeFormat
//    private LocalDate birthday;

    private String email;

    private String address;

    @Column
    @OneToMany(mappedBy ="owner",fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Contract> contracts=new HashSet<>();

    @Override
    public String toString(){
        return name+" "+surname;
    }

    public void addContract(Contract contract){
        contracts.add(contract);
    }
}
