package services;

import entities.Client;
import entities.NumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.GenericDAO;
import repositories.IGenericDAO;

@Service
public class PhoneNumberService {
    IGenericDAO<NumberGenerator>  numberGeneratorDAO;
    private static final int GENERATOR_ID=1;

    @Autowired
    public void setOptionDAO(GenericDAO<NumberGenerator> numberGeneratorDAO ) {
        this.numberGeneratorDAO=numberGeneratorDAO;
        numberGeneratorDAO.setClass(NumberGenerator.class);
    }

    @Transactional
    public long getNext(){
        NumberGenerator generator=numberGeneratorDAO.findOne(GENERATOR_ID);
        long phone=generator.getNextNumber();
        numberGeneratorDAO.update(generator);
        return phone;
    }
}
