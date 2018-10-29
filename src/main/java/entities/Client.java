package entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name="is_passport_exists",query = "select c.passportId from Client c where c.passportId=:id"),
        @NamedQuery(name="is_email_exists",query = "select c.email from Client c where c.email=:email"),
        @NamedQuery(name="find_by_passport",query = "from Client c where c.passportId=:passport"),
        @NamedQuery(name="find_by_email",query = "from Client c where c.email=:email")

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

    @Override
    public String toString(){
        return name+" "+surname;
    }

    public void addContract(Contract contract){
        contracts.add(contract);
    }
}
