package entities.dto;

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
import javax.validation.constraints.Pattern;

@Getter @Setter @NoArgsConstructor
@Validated
public class Phone {
    @Pattern(regexp = "^[0-9]{10}",message = "{phone.phoneNumber.invalid}")
    String phoneNumber;
}
