package com.airbnb.service;

import com.airbnb.dto.LoginDto;
import com.airbnb.dto.PropertyUserDto;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private JWTService jwtService;
    private PropertyUserRepository userRepository;

    public UserService(JWTService jwtService, PropertyUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public PropertyUser addUser(PropertyUserDto propertyUserDto){
        PropertyUser user=new PropertyUser();
        user.setFirstName(propertyUserDto.getFirstName());
        user.setLastName(propertyUserDto.getLastName());
        user.setUsername(propertyUserDto.getUsername());
        user.setEmail(propertyUserDto.getEmail());
        //two ways to encrypt password
        //1
        // user.setPassword(new BCryptPasswordEncoder().encode(propertyUserDto.getPassword()));
        //2
        user.setPassword(BCrypt.hashpw(propertyUserDto.getPassword(),BCrypt.gensalt(10)));
        user.setUserRole(propertyUserDto.getUserRole());
        PropertyUser savedUser = userRepository.save(user);
        return savedUser;
    }

    public String verifyLogin(LoginDto loginDto) {
        Optional<PropertyUser> opUser = userRepository.findByUsername(loginDto.getUsername());
        if(opUser.isPresent()){
            PropertyUser user = opUser.get();
             if(BCrypt.checkpw(loginDto.getPassword(), user.getPassword())){
                 //after login we generate jwt token for same user
                 return jwtService.generateToken(user);
             }
        }
        return null;
    }
}
