package entities.dto;

import entities.TariffOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
public class TariffOptionDTO {

    private int id;

    @NotBlank(message = "{option.name.empty}")
    @Size(min = 3, max = 50, message = "{option.name.invalid}")
    private String name;

    @Min(value = 0, message = "{option.price.invalid}")
    private int price;

    @Min(value = 0, message = "{option.subscribeCost.invalid}")
    private int subscribeCost;

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
