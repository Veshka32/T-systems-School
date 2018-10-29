package entities.dto;

import entities.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ContractDTO {
    private int id;

    private String number;

    private int ownerId;

    private String tariffName;

    private Set<String> optionsNames = new HashSet<>();

    private boolean isBlocked = false;
    private boolean isBlockedByAdmin = false;

    public ContractDTO(int clientId) {
        this.ownerId=clientId;
    }

    @Override
    public String toString() {
        return "number: " + number;
    }
}
