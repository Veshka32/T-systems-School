
package entities;

import entities.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Entity @Getter @Setter
@Table(name = "user")
@NamedQuery(name = "find_user_by_login",query = "from MyUser u where u.login=:login")
public class MyUser extends AbstractEntity {

    @NotBlank
    @Column(nullable = false)
    private String login;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @JoinTable(name = "user_roles",
            joinColumns = { @JoinColumn(name = "user_id") })
    private Set<Role> roles=new HashSet<>();

    @OneToOne(mappedBy = "user")
    private Contract contract;

    public void setRole(Role role){
        roles.add(role);
    }
}
