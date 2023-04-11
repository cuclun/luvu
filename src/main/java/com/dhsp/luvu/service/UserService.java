package com.dhsp.luvu.service;

import com.dhsp.luvu.dto.request.SignupRequest;
import com.dhsp.luvu.entity.User;

import java.util.List;

public interface UserService {

    User register(SignupRequest signupRequest);

    List<User> findAll();

}
