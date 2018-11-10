
package model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.enums.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
@NamedQuery(name = "find_user_by_login",query = "from MyUser u where u.login=:login")
public class MyUser extends AbstractEntity {

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @JoinTable(name = "user_roles",
            joinColumns = { @JoinColumn(name = "user_id") })
    private Set<Role> roles=new HashSet<>();

    @OneToOne
    private Contract contract;

    public void setRole(Role role){
        roles.add(role);
    }
}
