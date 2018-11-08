package entities.dto;

import entities.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ClientDTO{

    private int id;

    @Pattern(regexp = "^[0-9]{10}",message = "{client.passportId.invalid}")
    private String passportId;

    @NotBlank(message = "{client.name.invalid}")
    @Length(max = 255)
    private String name;

    @NotBlank(message = "{client.surname.invalid}")
    @Length(max = 255)
    private String surname;

    @Past(message = "{client.birthday.invalid}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;

    @Email(message = "{client.email.invalid}")
    @Length(max = 255)
    private String email;

    @Length(max = 255)
    private String address;

    public ClientDTO(Client client){
        id=client.getId();
        name=client.getName();
        surname=client.getSurname();
        birthday=client.getBirthday();
        passportId=client.getPassportId();
        email=client.getEmail();
        address=client.getAddress();
    }
}
