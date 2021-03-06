/**
 * This class is used to validate phone number format on view using spring validation.
 * It must be exactly 10 digits.
 */
package model.helpers;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@Validated
public class Phone {
    @Pattern(regexp = "^[0-9]{10}")
    private String phoneNumber;

    public Phone() {
        //default constructor
    }

    public @Pattern(regexp = "^[0-9]{10}") String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(@Pattern(regexp = "^[0-9]{10}") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
