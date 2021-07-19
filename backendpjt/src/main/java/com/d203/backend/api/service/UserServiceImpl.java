package com.d203.backend.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.d203.backend.api.request.UserRegisterPostReq;
import com.d203.backend.db.entity.User;
import com.d203.backend.db.repository.UserRepository;


/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	

	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public User createUser(UserRegisterPostReq userRegisterInfo) {
		User user = new User();
		user.setId(userRegisterInfo.getId());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User getUserByUserId(String id) {
		// 디비에 유저 정보 조회 (userId 를 통한 조회).
		User user = userRepository.findById(id).get();
		return user;
	}
}
