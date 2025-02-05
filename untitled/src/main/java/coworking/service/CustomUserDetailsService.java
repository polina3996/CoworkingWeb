package coworking.service;

import coworking.model.UserEntity;
import coworking.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to load user: " + username);
        UserEntity user = userEntityRepository.findByName(username);

        if (user == null){
            System.out.println("User not found!");
            //throw new BadCredentialsException("UserEntity not found");
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println("Loaded user: " + user.getName());

        // Create SimpleGrantedAuthority for the role with the prefix "ROLE_"
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                java.util.Collections.singletonList(authority) // Use a single authority (role)
        );

    }
}