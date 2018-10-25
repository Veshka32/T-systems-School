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
public class Client extends AbstractEntity{

    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{10}")
    private String passportId;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

//    @Past
//    @DateTimeFormat
//    private LocalDate birthday;

    @Email
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
