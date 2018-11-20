
package model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = "^[0-9]{10}")
    private String login;

    @NotBlank
    @Size(max = 255)
    private String password;

    private int contractId;
}
