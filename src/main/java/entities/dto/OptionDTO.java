package entities.dto;

import entities.TariffOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
public class TariffOptionDTO {

    private int id;

    @NotBlank(message = "{option.name.empty}")
    @Size(min = 3, max = 50, message = "{option.name.invalid}")
    private String name;

    @DecimalMin(value = "0.00", message = "{option.price.invalid}")
    @Digits(integer = 6,fraction = 2)
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "{option.subscribeCost.invalid}")
    @Digits(integer = 6,fraction = 2)
    private BigDecimal subscribeCost;

    private String description;

    private Set<String> incompatible = new HashSet<>();

    private Set<String> mandatory = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TariffOptionDTO(TariffOption option){
        id=option.getId();
        name=option.getName();
        price=option.getPrice();
        subscribeCost=option.getSubscribeCost();
        description=option.getDescription();
    }
}
