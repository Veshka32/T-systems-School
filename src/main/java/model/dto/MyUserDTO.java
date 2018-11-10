
package model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@NoArgsConstructor
public class MyUserDTO {

    @NotBlank
    @Length(max = 255)
    @Pattern(regexp = "^[0-9]{10}", message = "{myUserDTO.login.invalid}")
    private String login;

    @NotBlank(message = "{myUserDTO.password.invalid}")
    @Length(max = 255)
    private String password;

    private int contractId;
}
