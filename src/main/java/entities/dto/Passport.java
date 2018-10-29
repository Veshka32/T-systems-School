package entities.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@Getter @Setter @NoArgsConstructor
@Validated
public class Passport {
    @Pattern(regexp = "^[0-9]{10}",message = "{passport.passport.invalid}")
    String passport;
}
