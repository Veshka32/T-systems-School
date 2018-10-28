package entities.dto;

import entities.Tariff;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


public class TariffDTO {

    private int id;

    @NotBlank(message = "{tariff.name.empty}")
    @Size(min = 3, max = 50, message = "{tariff.name.invalid}")
    private String name;

    @Min(value = 0, message = "{tariff.price.invalid}")
    private int price;

    private boolean archived;

    private String description;

    private Set<String> options = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TariffDTO(){}

    public TariffDTO(Tariff option){
        id=option.getId();
        name=option.getName();
        price=option.getPrice();
        archived=option.isArchived();
        description=option.getDescription();
    }


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

    public Set<String> getOptions() {
        return options;
    }

    public void setOptions(Set<String> options) {
        this.options = options;
    }
}
