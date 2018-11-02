package entities.dto;

import entities.Tariff;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Getter @Setter
public class TariffDTO {

    private int id;

    @NotBlank(message = "{tariff.name.empty}")
    @Size(min = 3, max = 50, message = "{tariff.name.invalid}")
    private String name;

    @Min(value = 0, message = "{tariff.price.invalid}")
    private int price;

    private String description;

    private Set<String> options = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TariffDTO(){}

    public TariffDTO(Tariff option){
        id=option.getId();
        name=option.getName();
        price=option.getPrice();
        description=option.getDescription();
    }
}
