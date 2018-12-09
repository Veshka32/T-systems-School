/**
 * This class implements {@code PhoneNumberServiceI} interface.
 * It is a service-layer class for generating unique phone number of specific format required in {@code Contract}.
 * <p>
 *
 * @author Natalia Makarchuk
 */
package services.implementations;

import dao.interfaces.PhoneNumberDaoI;
import model.entity.NumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.interfaces.PhoneNumberServiceI;

@Service
@EnableTransactionManagement
@Transactional
public class PhoneNumberService implements PhoneNumberServiceI {

    private PhoneNumberDaoI numberGeneratorDAO;
    private static final int GENERATOR_ID = 1; //id of target NumberGenerator entity in database

    @Autowired
    public void setNumberGeneratorDAO(PhoneNumberDaoI numberGeneratorDAO) {
        this.numberGeneratorDAO=numberGeneratorDAO;
        this.numberGeneratorDAO.setClass(NumberGenerator.class);
    }

    /**
     * Generate next unique phone number by incrementing current number in corresponding NumberGenerator by 1 and save its new value.
     * At most one thread can read and update same NumberGenerator at the same time
     * @return new unique phone number
     */
    @Override
    public long getNext() {
        NumberGenerator generator = numberGeneratorDAO.getForUpdate(GENERATOR_ID); //with LockMode.Pessimistic_Read
        long phone=generator.getNextNumber();
        numberGeneratorDAO.update(generator);
        return phone;
    }
}
