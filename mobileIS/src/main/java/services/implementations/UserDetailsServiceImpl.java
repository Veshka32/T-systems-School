/**
 * This class implements {@code UserDetailService} interface used in SpringSecurity module
 */
package services.implementations;

import dao.interfaces.UserDaoI;
import model.entity.User;
import model.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EnableTransactionManagement
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDaoI userDAO;

    /**
     * Return UserDetails of a user specified by username if such a user exists,
     * contains his granted authorities, login and hashed password
     *
     * @param username username of desired user
     * @return UserDetails object
     * @throws UsernameNotFoundException if there is no user with such a username
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {

        User user = userDAO.findByLogin(username);

        if (user == null) throw new UsernameNotFoundException("User not found by name: " + username);
        List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private org.springframework.security.core.userdetails.User buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }


    private List<GrantedAuthority> buildUserAuthority(Set<Role> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<>();

        for (Role role : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(role.toString()));
        }

        return new ArrayList<>(setAuths);
    }
}
