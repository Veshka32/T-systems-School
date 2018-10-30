package services.interfaces;

import org.springframework.transaction.annotation.Transactional;

public interface PhoneNumberServiceI {
    @Transactional
    long getNext();
}
