package model.dto;

import model.entity.Client;
import model.entity.Contract;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

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

    public ClientDTO() {
    }

    public ClientDTO(Client client){

        id=client.getId();
        name=client.getName();
        surname=client.getSurname();
        birthday=client.getBirthday();
        passportId=client.getPassportId();
        email=client.getEmail();
        address=client.getAddress();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
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

    public List<Contract> getContractsList() {
        return contractsList;
    }

    public void setContractsList(List<Contract> contractsList) {
        this.contractsList = contractsList;
    }
}
