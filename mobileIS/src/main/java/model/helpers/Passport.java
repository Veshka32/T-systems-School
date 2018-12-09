/**
 * This class is used to validate passport format on view using spring validation.
 * It must be exactly 10 digits.
 */
package model.helpers;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@Validated
public class Passport {
    @Pattern(regexp = "^[0-9]{10}")
    private String passportNumber;

    public Passport() {
        //default constructor
    }

    public @Pattern(regexp = "^[0-9]{10}") String getPassportNumber() {
        return this.passportNumber;
    }

    public void setPassportNumber(@Pattern(regexp = "^[0-9]{10}") String passportNumber) {
        this.passportNumber = passportNumber;
    }
}
