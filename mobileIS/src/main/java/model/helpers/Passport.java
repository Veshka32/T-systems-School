package model.helpers;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@Validated
public class Passport {
    @Pattern(regexp = "^[0-9]{10}")
    String passportNumber;

    public Passport() {
    }

    public @Pattern(regexp = "^[0-9]{10}") String getPassportNumber() {
        return this.passportNumber;
    }

    public void setPassportNumber(@Pattern(regexp = "^[0-9]{10}") String passportNumber) {
        this.passportNumber = passportNumber;
    }
}
