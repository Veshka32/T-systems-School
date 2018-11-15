package model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.entity.Contract;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ContractDTO {
    private Integer id;

    private String number;

    private int ownerId;

    private String ownerName;

    private String tariffName;
    private int tariffId;

    private Set<Integer> optionsIds = new HashSet<>();
    private String optionNames;

    private boolean isBlocked = false;
    private boolean isBlockedByAdmin = false;

    public ContractDTO(int clientId) {
        this.ownerId=clientId;
    }

    public ContractDTO(Contract contract){
        this.id=contract.getId();
        this.number=contract.getNumber()+"";
        this.ownerId=contract.getOwner().getId();
        this.ownerName = contract.getOwner().toString();
        this.tariffId = contract.getTariff().getId();
        this.tariffName=contract.getTariff().getName();
        this.isBlocked=contract.isBlocked();
        this.isBlockedByAdmin=contract.isBlockedByAdmin();
    }

    @Override
    public String toString() {
        return "number: " + number;
    }
}
