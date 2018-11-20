package model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.entity.Client;
import model.entity.Contract;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ClientDTO{

    private Integer id;

    @Pattern(regexp = "^[0-9]{10}")
    private String passportId;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String surname;

    @Past
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;

    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String address;

    private List<Contract> contractsList;

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
