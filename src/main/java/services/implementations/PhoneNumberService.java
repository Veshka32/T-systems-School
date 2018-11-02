package services.implementations;

import entities.helpers.NumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.interfaces.IGenericDAO;
import services.interfaces.PhoneNumberServiceI;

@Service
public class PhoneNumberService implements PhoneNumberServiceI {
    IGenericDAO<NumberGenerator>  numberGeneratorDAO;
    private static final int GENERATOR_ID=1;

    @Autowired
    public void setOptionDAO(IGenericDAO<NumberGenerator> numberGeneratorDAO ) {
        this.numberGeneratorDAO=numberGeneratorDAO;
        numberGeneratorDAO.setClass(NumberGenerator.class);
    }

    @Override
    @Transactional
    public long getNext(){
        NumberGenerator generator=numberGeneratorDAO.findOne(GENERATOR_ID);
        long phone=generator.getNextNumber();
        numberGeneratorDAO.update(generator);
        return phone;
    }
}
