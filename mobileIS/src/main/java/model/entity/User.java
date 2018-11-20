
package model.entity;

import model.enums.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "user")
@NamedQuery(name = "find_user_by_login", query = "from User u where u.login=:login")
public class User extends AbstractEntity {

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

    public User() {
    }

    public void setRole(Role role){
        roles.add(role);
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
