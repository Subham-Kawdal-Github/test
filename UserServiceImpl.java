package com.incident.management.service;

import com.incident.management.config.JwtTokenProvider;
import com.incident.management.dto.ForgotPasswordRequest;
import com.incident.management.dto.LoginRequest;
import com.incident.management.dto.UserDto;
import com.incident.management.entity.User;
import com.incident.management.exception.ResourceNotFoundException;
import com.incident.management.exception.UnauthorizedException;
import com.incident.management.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PincodeService pincodeService;
    
    public UserServiceImpl(UserRepository userRepository, 
                         PasswordEncoder passwordEncoder,
                         AuthenticationManager authenticationManager,
                         JwtTokenProvider jwtTokenProvider,
                         PincodeService pincodeService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.pincodeService = pincodeService;
    }
    
    @Override
    public UserDto register(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        
        // Fetch city using pincode
        if (userDto.getPinCode() != null && !userDto.getPinCode().isEmpty()) {
            String city = pincodeService.getCityByPincode(userDto.getPinCode());
            userDto.setCity(city);
        }
        
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        user.setPinCode(userDto.getPinCode());
        user.setCity(userDto.getCity());
        user.setCountry(userDto.getCountry());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        User savedUser = userRepository.save(user);
        
        userDto.setId(savedUser.getId());
        userDto.setPassword(null); // Don't return password
        
        return userDto;
    }
    
    @Override
    public String login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtTokenProvider.createToken(loginRequest.getEmail());
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid email or password");
        }
    }
    
    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
    
    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setAddress(user.getAddress());
        userDto.setPinCode(user.getPinCode());
        userDto.setCity(user.getCity());
        userDto.setCountry(user.getCountry());
        
        return userDto;
    }
    
    @Override
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }
}