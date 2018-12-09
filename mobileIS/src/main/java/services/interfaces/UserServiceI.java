package services.interfaces;

import model.dto.AccountDTO;

import java.util.Optional;

public interface UserServiceI {
    /**
     * Create new account and save it to database
     *
     * @param dto data transfer object contains new account properties
     * @return id of newly created {@code User}
     */
    Optional<String> createClientAccount(AccountDTO dto);
}
