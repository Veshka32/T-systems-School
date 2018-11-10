package services.implementations;

import dao.interfaces.IGenericDAO;
import model.helpers.NumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.interfaces.PhoneNumberServiceI;

@Service
@EnableTransactionManagement
@Transactional
public class PhoneNumberService implements PhoneNumberServiceI {
    private IGenericDAO<NumberGenerator> numberGeneratorDAO;
    private static final int GENERATOR_ID=1;

    @Autowired
    public void setNumberGeneratorDAO(IGenericDAO<NumberGenerator> numberGeneratorDAO) {
        this.numberGeneratorDAO=numberGeneratorDAO;
        this.numberGeneratorDAO.setClass(NumberGenerator.class);
    }

    @Override
    public long getNext(){
        NumberGenerator generator=numberGeneratorDAO.findOne(GENERATOR_ID);
        long phone=generator.getNextNumber();
        numberGeneratorDAO.update(generator);
        return phone;
    }
}
