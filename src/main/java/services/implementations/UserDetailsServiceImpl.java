package services.implementations;

import dao.interfaces.UserDaoI;
import entities.MyUser;
import entities.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EnableTransactionManagement
@Transactional
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDaoI userDAO;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {

        MyUser user = userDAO.findByLogin(username);
        List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private User buildUserForAuthentication(MyUser user, List<GrantedAuthority> authorities) {
        return new User(user.getLogin(), user.getPassword(), authorities);
    }


    private List<GrantedAuthority> buildUserAuthority(Set<Role> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<>();

        for (Role role : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(role.toString()));
        }

        return new ArrayList<>(setAuths);
    }


}
