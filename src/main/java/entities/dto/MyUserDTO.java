
package entities.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
public class MyUserDTO {

    @NotBlank
    @Column(nullable = false)
    private String login;

    @NotNull
    @Column(nullable = false)
    private String password;

    private int contractId;
}
