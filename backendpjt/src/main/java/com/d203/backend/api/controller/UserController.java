package com.d203.backend.api.controller;

import com.d203.backend.api.service.Email.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.d203.backend.api.request.User.UserDeleteReq;
import com.d203.backend.api.request.User.UserReq;
import com.d203.backend.api.response.User.UserRes;
import com.d203.backend.api.service.User.UserService;
import com.d203.backend.common.auth.SsafyUserDetails;
import com.d203.backend.common.model.response.BaseResponseBody;
import com.d203.backend.db.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Value("${isDev}")
    boolean isDev;

    @Autowired
    UserService userService;

    @Autowired
    EmailSenderService emailSenderService;


    @PutMapping(value = "/saveimg/{userId}")
    public ResponseEntity<String> profileImg(@PathVariable String userId,
                                             @RequestPart("imgFile") MultipartFile imgFile) throws IOException {

        System.out.println("[isDev]" + isDev);

        System.out.println("이미지 수정");
        String basePath, frontPath, fileName, filePath;

        if (isDev) {
            // 개발 서버 설정
            String[] splitPath =
                    (new File("").getCanonicalPath().split("\\\\"));

            int splitPathLen = splitPath.length;

            // 프로젝트경로
            basePath = String.join("/", Arrays.copyOfRange(splitPath, 0,
                    splitPathLen - 1));

            frontPath = "/front/src/assets/image/profile/";
        } else {
            // 서버단 저장될 위치
            basePath = "/volumes/profile/";
            frontPath="";
        }

        fileName = userId.toString() + "_profileImg.jpg";

        //경로설정
        filePath = basePath + frontPath + fileName;

        File dest = new File(filePath);

        //파일 생성
        imgFile.transferTo(dest);

        // DB에 이미지 원본 이름만 저장
        Long userid = Long.parseLong(userId);
        if (userService.saveImgFile(userid, fileName)) {
            return ResponseEntity.status(200).body(imgFile.getOriginalFilename());
        }

        return ResponseEntity.status(500).body("이미지저장 실패");
    }


    @PostMapping()
    @ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends BaseResponseBody> register(
            @RequestBody @ApiParam(value = "회원가입 정보", required = true) UserReq registerInfo) {
        //임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.

        User user = userService.createUser(registerInfo);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));


    }

    @GetMapping("/checkemail/{email}")
    @ApiOperation(value = "이메일 중복 확인조회", notes = "이메일 중복을 확인한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 409, message = "이메일 중복")
    })
    public ResponseEntity<? extends BaseResponseBody> checkDupEmail(@PathVariable String email) {

        //check branch
        if (userService.checkEmail(email)) {
            System.out.println("User email dup");
            return ResponseEntity.status(409).body(BaseResponseBody.of(409, "Duplicate Error"));

        } else {
            System.out.println("success");
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
        }

    }

    @GetMapping("/checknick/{nickname}")
    @ApiOperation(value = "닉네임 중복 확인조회", notes = "닉네임 중복을 확인한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 409, message = "닉네임 중복")
    })
    public ResponseEntity<? extends BaseResponseBody> checkDupNickname(@PathVariable String nickname) {

        //check branch
        if (userService.checkNick(nickname)) {
            System.out.println("User nickname dup");
            return ResponseEntity.status(409).body(BaseResponseBody.of(409, "Duplicate Error"));

        } else {
            System.out.println("success");
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
        }

    }

    @GetMapping("/me")
    @ApiOperation(value = "회원 본인 정보 조회", notes = "로그인한 회원 본인의 정보를 응답한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<UserRes> getUserInfo(@ApiIgnore Authentication authentication) {
        /**
         * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
         * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
         */


        SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
        System.out.println("userDetails : success" + userDetails.getUsername());
        String email = userDetails.getUsername();
        User user = userService.getUserByEamil(email);
        return ResponseEntity.status(200).body(UserRes.of(user));
    }

    @GetMapping("{user_id}")
    @ApiOperation(value = "유저 정보 조회", notes = "유저의 pk 값을 통해 유저 정보를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<UserRes> getUserInfo(@PathVariable Long user_id) {
        /**
         * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
         * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
         */
        User user = userService.getUserByuserId(user_id);

        return ResponseEntity.status(200).body(UserRes.of(user));
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserReq userReq) {
        System.out.println("update" + userReq.toString());
        if (userService.updateUser(userReq)) {
            return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
        }
        return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
    }

    //회원 삭제
    @DeleteMapping()
    public ResponseEntity<?> deleteUser(@RequestBody UserDeleteReq userDeleteReq) {
        if (userService.deleteUser(userDeleteReq.getEmail(), userDeleteReq.getPassword())) {
            return ResponseEntity.status(204).body("User delete success");
        }
        return ResponseEntity.status(404).body("User password not macthed");
    }

}
