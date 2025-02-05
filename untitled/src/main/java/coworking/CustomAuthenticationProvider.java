package coworking;

import coworking.model.UserEntity;
import coworking.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserEntityRepository userRepository; // Assuming you have a repository to fetch user data

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws BadCredentialsException {
        String username = authentication.getName(); // Get username from the Authentication object
        String password = (String) authentication.getCredentials(); // Get password from the Authentication object

        // Fetch the user from the database
        UserEntity user = userRepository.findByName(username);

        // Check if user exists and password is correct
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        // Create a GrantedAuthority from the role (add "ROLE_" prefix)
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

        // Create and return the authentication token
        UserDetails userDetails = new User(user.getName(), user.getPassword(),  AuthorityUtils.createAuthorityList(authority.getAuthority()));
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}