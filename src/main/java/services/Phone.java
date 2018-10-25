package services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter @Setter @NoArgsConstructor
@Validated
public class Phone {
    @Range(min = 1000000000L,max=9999999999L,message = "must be 10 digits")
    long phoneNumber;
}
