package entities.dto;

import entities.Tariff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class TariffDTO {

    private int id;

    @NotBlank(message = "{tariff.name.empty}")
    @Size(min = 3, max = 50, message = "{tariff.name.invalid}")
    private String name;

    @DecimalMin(value = "0.00", message = "{tariff.price.invalid}")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price;

    private String description;

    private Set<String> options = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TariffDTO(Tariff option){
        id=option.getId();
        name=option.getName();
        price=option.getPrice();
        description=option.getDescription();
    }
}
