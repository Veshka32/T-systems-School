package model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.entity.Tariff;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class TariffDTO {

    private Integer id;

    @NotBlank(message = "{tariff.name.empty}")
    @Length(min = 3, max = 255, message = "{tariff.name.invalid}")
    private String name;

    @DecimalMin(value = "0.00", message = "{tariff.price.invalid}")
    @Digits(integer = 6, fraction = 2, message = "{tariff.price.invalid}")
    @NotNull(message = "{tariff.price.invalid}")
    private BigDecimal price;

    @Length(max = 255)
    private String description;

    private Set<Integer> options = new HashSet<>();
    private String optionsNames;

    public TariffDTO(Tariff tariff) {
        id = tariff.getId();
        name = tariff.getName();
        price = tariff.getPrice();
        description = tariff.getDescription();
    }
}
