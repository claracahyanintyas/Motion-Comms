package org.individualproject.semester3project.usecase.security.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.security.dto.JwtAuthResponse;
import org.individualproject.semester3project.security.JwtTokenProvider;
import org.individualproject.semester3project.usecase.mapper.UserMapper;
import org.individualproject.semester3project.usecase.security.AuthService;
import org.individualproject.semester3project.usecase.security.dto.JwtAuthOutput;
import org.individualproject.semester3project.usecase.security.dto.LoginInput;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;

    @Override
    public JwtAuthOutput login(LoginInput loginInput) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginInput.getUsernameOrEmail(),
                loginInput.getPassword()
        ));

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        String token = jwtTokenProvider.generateToken(authentication);
        jwtAuthResponse.setAccessToken(token);

        User user = userRepository.findByUsername(loginInput.getUsernameOrEmail()).fromEntity();
        UserPublicData userPublicData = UserMapper.mapUserToUserPublicData(user);
        jwtAuthResponse.setUser(userPublicData);


        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtAuthResponse;
    }
}