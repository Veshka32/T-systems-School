
package entities.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
public class MyUserDTO {

    @NotBlank
    @Length(max = 255)
    private String login;

    @NotNull
    @Length(max = 255)
    private String password;

    private int contractId;
}
