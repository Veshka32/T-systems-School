package services.interfaces;

import model.dto.AccountDTO;
import services.exceptions.AccountCreateException;

public interface AccountServiceI {
    /**
     * Create new account and save it to database
     *
     * @param dto data transfer object contains new account properties
     * @return id of newly created {@code Account}
     * @throws AccountCreateException if any data are invalid
     */
    int createAccount(AccountDTO dto) throws AccountCreateException;
}
