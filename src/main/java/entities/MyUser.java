
package entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity @Getter @Setter
@Table(name = "user")
public class MyUser implements Serializable {

    @Id @GeneratedValue
    private int id;

    @NotBlank
    @Column(nullable = false)
    @NaturalId
    private String login;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @JoinTable(name = "user_roles",
            joinColumns = { @JoinColumn(name = "user_id") })
    private Set<Role> roles=new HashSet<>();
}
