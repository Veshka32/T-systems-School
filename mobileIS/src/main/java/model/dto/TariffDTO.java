package model.dto;

import model.entity.Tariff;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class TariffDTO {

    private Integer id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price = new BigDecimal(0);

    @Size(max = 255)
    private String description;

    private Set<Integer> options = new HashSet<>();
    private String optionsNames;

    public TariffDTO() {
        //default constructor
    }

    public TariffDTO(Tariff tariff) {
        id = tariff.getId();
        name = tariff.getName();
        price = tariff.getPrice();
        description = tariff.getDescription();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Integer> getOptions() {
        return options;
    }

    public void setOptions(Set<Integer> options) {
        this.options = options;
    }

    public String getOptionsNames() {
        return optionsNames;
    }

    public void setOptionsNames(String optionsNames) {
        this.optionsNames = optionsNames;
    }
}
