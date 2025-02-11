package coworking;

import coworking.config.JwtUtil;
import coworking.dto.AuthRequest;
import coworking.dto.RegisterRequest;
import coworking.model.UserEntity;
import coworking.repository.UserEntityRepository;
import coworking.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    private RegisterRequest registerRequest = new RegisterRequest();
    private AuthRequest authRequest = new AuthRequest();
    private UserEntity userEntity = new UserEntity();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        registerRequest.setUsername("testUser");
        registerRequest.setPassword("password");
        registerRequest.setRole("USER");

        authRequest.setUsername("testUser");
        authRequest.setPassword("password");

        userEntity.setName("testUser");
        userEntity.setPassword("encodedPassword");
        userEntity.setRole("ROLE_USER");
    }

    @Test
    public void registerUser_whenUsernameNotExist_thenReturnSuccess() throws Exception {
        when(userEntityRepository.findByName(anyString())).thenReturn(null);
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\",\"password\":\"password\",\"role\":\"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        verify(userEntityRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void registerUser_whenUsernameExists_thenReturnError() throws Exception {
        when(userEntityRepository.findByName(anyString())).thenReturn(userEntity);

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\",\"password\":\"password\",\"role\":\"USER\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));

        verify(userEntityRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void login_whenCredentialsAreValid_thenReturnJwt() throws Exception {
        String jwt = "mock-jwt-token";

        when(userEntityRepository.findByName(anyString())).thenReturn(userEntity);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(jwtUtil.generateToken(any(User.class))).thenReturn(jwt);

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwt));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void login_whenCredentialsAreInvalid_thenReturnUnauthorized() throws Exception {
        when(userEntityRepository.findByName(anyString())).thenReturn(userEntity);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\",\"password\":\"wrongPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials")); // Expect error message

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
