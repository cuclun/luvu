package com.dhsp.luvu.controller;

import com.dhsp.luvu.dto.request.SigninRequest;
import com.dhsp.luvu.dto.request.SignupRequest;
import com.dhsp.luvu.dto.response.JwtResponse;
import com.dhsp.luvu.dto.response.MessageResponse;
import com.dhsp.luvu.entity.User;
import com.dhsp.luvu.security.jwt.JwtProvider;
import com.dhsp.luvu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rest/auth")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserService userService;

    @GetMapping(value = {"/", ""})
    @PreAuthorize("hasRole('ADMIN')")// để dùng cái ni
    public ResponseEntity<?> getAllAccount() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            User user = userService.register(signupRequest);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest signinRequest) { //gửi yêu cầu đăng nhập

        // xác thực tài khoản
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //tạo 1 accesstoken để khi thực hiện các chức năng của MOD hoặc ADMIN
        // thì hắn sẽ mã hóa lại cái dãy nớ để coi là cái chi mà thực hiện chức năng mô
        String jwt = jwtProvider.generateJwtToken(authentication);

        // lưu thông tin đã đăng nhập
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //trả về 1 object gồm...
        return new ResponseEntity<>(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities(), jwtProvider.getJwtExpiration()), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAccount(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(userService.delete(id), HttpStatus.OK);
    }
}
