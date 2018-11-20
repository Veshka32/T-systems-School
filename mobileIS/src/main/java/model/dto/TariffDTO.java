package model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.entity.Tariff;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class TariffDTO {

    private Integer id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2)
    @NotNull
    private BigDecimal price;

    @Size(max = 255)
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
