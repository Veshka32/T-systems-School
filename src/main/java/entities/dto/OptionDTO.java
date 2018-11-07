package entities.dto;

import entities.Option;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
public class OptionDTO {

    private int id;

    @NotBlank(message = "{option.name.empty}")
    @Size(min = 3, max = 50, message = "{option.name.invalid}")
    private String name;

    @DecimalMin(value = "0.00", message = "{option.price.invalid}")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "{option.subscribeCost.invalid}")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal subscribeCost;

    @Length(max = 255)
    private String description;

    private Set<String> incompatible = new HashSet<>();

    private Set<String> mandatory = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OptionDTO(Option option) {
        id = option.getId();
        name = option.getName();
        price = option.getPrice();
        subscribeCost = option.getSubscribeCost();
        description = option.getDescription();
    }
}
