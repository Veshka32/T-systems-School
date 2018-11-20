package model.dto;

import model.entity.Option;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class OptionDTO {

    private Integer id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2)
    @NotNull
    private BigDecimal price;

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2)
    @NotNull
    private BigDecimal subscribeCost;

    @Size(max = 255)
    private String description;

    private Set<Integer> incompatible = new HashSet<>();

    private Set<Integer> mandatory = new HashSet<>();

    private String incompatibleNames;

    private String mandatoryNames;

    public OptionDTO(Option option) {
        id = option.getId();
        name = option.getName();
        price = option.getPrice();
        subscribeCost = option.getSubscribeCost();
        description = option.getDescription();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
