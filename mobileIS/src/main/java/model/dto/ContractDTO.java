package model.dto;
import model.entity.Contract;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ContractDTO {
    private Integer id;

    private String number;

    private int ownerId;

    private String ownerName;

    private String tariffName;
    private int tariffId;

    private Set<Integer> optionsIds = new HashSet<>();
    private String optionNames;

    Map<String, Integer> allOptions = new HashMap<>();
    Map<String, Integer> allTariffs = new HashMap<>();

    private boolean isBlocked = false;
    private boolean isBlockedByAdmin = false;

    public ContractDTO() {
        //default constructor
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public int getTariffId() {
        return tariffId;
    }

    public void setTariffId(int tariffId) {
        this.tariffId = tariffId;
    }

    public Set<Integer> getOptionsIds() {
        return optionsIds;
    }

    public void setOptionsIds(Set<Integer> optionsIds) {
        this.optionsIds = optionsIds;
    }

    public String getOptionNames() {
        return optionNames;
    }

    public void setOptionNames(String optionNames) {
        this.optionNames = optionNames;
    }

    public Map<String, Integer> getAllOptions() {
        return allOptions;
    }

    public void setAllOptions(Map<String, Integer> allOptions) {
        this.allOptions = allOptions;
    }

    public Map<String, Integer> getAllTariffs() {
        return allTariffs;
    }

    public void setAllTariffs(Map<String, Integer> allTariffs) {
        this.allTariffs = allTariffs;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isBlockedByAdmin() {
        return isBlockedByAdmin;
    }

    public void setBlockedByAdmin(boolean blockedByAdmin) {
        isBlockedByAdmin = blockedByAdmin;
    }

    @Override
    public String toString() {
        return "number: " + number;
    }
}
