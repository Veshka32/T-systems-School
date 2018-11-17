package services.interfaces;



public interface PhoneNumberServiceI {
    /**
     * Generate next unique phone number for new {@code Contract}
     *
     * @return phone number
     */
    long getNext();
}
