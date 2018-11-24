package model.dto;

import model.entity.Option;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    private BigDecimal price = new BigDecimal(0);

    @DecimalMin(value = "0.00")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal subscribeCost = new BigDecimal(0);

    @Size(max = 255)
    private String description;

    private Set<Integer> incompatible = new HashSet<>();

    private Set<Integer> mandatory = new HashSet<>();

    private String incompatibleNames;

    private String mandatoryNames;

    public OptionDTO() {
    }
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

    public BigDecimal getSubscribeCost() {
        return subscribeCost;
    }

    public void setSubscribeCost(BigDecimal subscribeCost) {
        this.subscribeCost = subscribeCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Integer> getIncompatible() {
        return incompatible;
    }

    public void setIncompatible(Set<Integer> incompatible) {
        this.incompatible = incompatible;
    }

    public Set<Integer> getMandatory() {
        return mandatory;
    }

    public void setMandatory(Set<Integer> mandatory) {
        this.mandatory = mandatory;
    }

    public String getIncompatibleNames() {
        return incompatibleNames;
    }

    public void setIncompatibleNames(String incompatibleNames) {
        this.incompatibleNames = incompatibleNames;
    }

    public String getMandatoryNames() {
        return mandatoryNames;
    }

    public void setMandatoryNames(String mandatoryNames) {
        this.mandatoryNames = mandatoryNames;
    }
}
