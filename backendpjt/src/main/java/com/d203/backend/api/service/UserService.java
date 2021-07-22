package com.d203.backend.api.service;

import com.d203.backend.api.request.UserReq;
import com.d203.backend.db.entity.User;



/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserService {
	User createUser(UserReq userRegisterInfo);
	User getUserByEamil(String email);
	//회원 수정
	boolean updateUser(UserReq userUpdateInfo);
	//회원 탈퇴
	boolean deleteUser(String email,String password);
	
	//이메일 중복체크
	boolean checkEmail(String email);
	
	//닉네임 중복체크 
	boolean checkNick(String nickname);

}
