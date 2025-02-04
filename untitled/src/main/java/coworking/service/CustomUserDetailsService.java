package coworking.service;

import coworking.model.UserEntity;
import coworking.repository.UserEntityRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    public CustomUserDetailsService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = this.userEntityRepository.findByName(username);

        if (user == null){
            throw new BadCredentialsException("UserEntity not found");
        }

        return User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}