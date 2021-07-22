package com.d203.backend.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.d203.backend.api.request.UserReq;
import com.d203.backend.db.entity.Lang;
import com.d203.backend.db.entity.User;
import com.d203.backend.db.repository.LangRepository;
import com.d203.backend.db.repository.UserRepository;

import java.util.Optional;


/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	LangRepository langRepository ;
	
	@Autowired
	PasswordEncoder passwordEncoder;


	@Override
	public User createUser(UserReq userRegisterInfo) {
		
		User user = new User();
		Lang lang = langRepository.findById(userRegisterInfo.getLang()).get();
		user.setLang(lang);
		user.setEmail(userRegisterInfo.getEmail());
		user.setNickname(userRegisterInfo.getNickname());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User getUserByEamil(String email) {

		Optional<User> user= userRepository.findByEmail(email);

		return user.orElse(null);
	}

	@Override
	public boolean updateUser(UserReq userUpdateInfo) {
		Optional<User> user = userRepository.findByEmail(userUpdateInfo.getEmail());
		//유저 정보가 있다면
		User updateUser = user.get();
		
		//updateUser.setLang(userUpdateInfo.getLang());
		updateUser.setNickname(userUpdateInfo.getNickname());
		updateUser.setPassword(passwordEncoder.encode(userUpdateInfo.getPassword()));
		
		if(user.isPresent()){
			//System.out.println(updateUser.toString());
			userRepository.save(updateUser);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteUser(String email , String password) {
		
		User user = userRepository.findByEmail(email).get();
		//System.out.println(userRepository.findByEmail(email));
		if (passwordEncoder.matches(password, user.getPassword())) {
			userRepository.delete(user);
			return true;
		} 
		else {
			return false;
		}
		
		
	}

	@Override
	public boolean checkEmail(String email) {
		//System.out.println("param email : " + email);
		Optional<User> user = userRepository.findByEmail(email);
		//System.out.println(userRepository.findByEmail(email));
		if (!user.isPresent()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean checkNick(String nickname) {
		//System.out.println("param nickname : " + nickname);
		Optional<User> user = userRepository.findByNickname(nickname);
		//System.out.println(userRepository.findByNickname(nickname));
		if (!user.isPresent()) {
			return false;
		} else {
			return true;
		}
	}


}
