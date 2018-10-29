package entities.dto;

import entities.Contract;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public ContractDTO(Contract contract){
        this.id=contract.getId();
        this.ownerId=contract.getOwner().getId();
        this.tariffName=contract.getTariff().getName();
        this.isBlocked=contract.isBlocked();
        this.isBlockedByAdmin=contract.isBlockedByAdmin();
    }

    @Override
    public String toString() {
        return "number: " + number;
    }
}
