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
    private static final int GENERATOR_ID=1;

    @Autowired
    public void setNumberGeneratorDAO(PhoneNumberDaoI numberGeneratorDAO) {
        this.numberGeneratorDAO=numberGeneratorDAO;
        this.numberGeneratorDAO.setClass(NumberGenerator.class);
    }

    @Override
    public long getNext(){
        NumberGenerator generator = numberGeneratorDAO.getForUpdate(GENERATOR_ID); //with LockMode.Pessimistic_Write
        long phone=generator.getNextNumber();
        numberGeneratorDAO.update(generator);
        return phone;
    }
}
