package entities;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;


public class TariffOptionDTO {

    private int id;

    @NotBlank(message = "{option.name.invalid}")
    @Size(min = 3, max = 50, message = "{option.name.invalid}")
    private String name;

    @Min(value = 0, message = "{option.price.invalid}")
    private int price;

    @Min(value = 0, message = "{option.subscribeCost.invalid}")
    private int subscribeCost;

    private boolean archived;

    private String description;

    private Set<Integer> incompatible = new HashSet<>();

    private Set<Integer> mandatory = new HashSet<>();

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
        archived=option.isArchived();
        description=option.getDescription();
    }

    public TariffOptionDTO(){    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSubscribeCost() {
        return subscribeCost;
    }

    public void setSubscribeCost(int subscribeCost) {
        this.subscribeCost = subscribeCost;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
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
}
