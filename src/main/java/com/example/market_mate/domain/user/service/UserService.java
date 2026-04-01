package com.example.market_mate.domain.user.service;

import com.example.market_mate.common.exception.user.DuplicateUserException;
import com.example.market_mate.common.exception.user.NotFoundUserException;
import com.example.market_mate.domain.user.entity.User;
import com.example.market_mate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public User createUser(String loginId, String loginPw, String name,
                           String phone, String storeName, String marketName) {
        validateDuplicateUser(loginId);
        User user = User.createUser(loginId, loginPw, name,
                phone, storeName, marketName, bCryptPasswordEncoder);
        return userRepository.save(user);
    }

    /**
     * 로그인
     */
    @Transactional
    public User login(String loginId, String loginPw) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new NotFoundUserException(
                        "존재하지 않는 아이디입니다.", HttpStatus.NOT_FOUND));
        user.isMatchPasswd(loginPw, bCryptPasswordEncoder);
        return user;
    }

    /**
     * 내 정보 조회
     */
    public User getMyInfo(Long userId) {
        return getUserById(userId);
    }

    /**
     * 비밀번호 확인
     */
    @Transactional
    public void isMatchPasswd(Long userId, String passwd) {
        User user = getUserById(userId);
        user.isMatchPasswd(passwd, bCryptPasswordEncoder);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePasswd(Long userId, String oldPasswd, String newPasswd) {
        User user = getUserById(userId);
        user.changePasswd(oldPasswd, newPasswd, bCryptPasswordEncoder);
        // User 엔티티에 changePasswd 메서드 추가 필요
    }

    /**
     * RefreshToken 업데이트
     */
    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken) {
        User user = getUserById(userId);
        user.updateRefreshToken(refreshToken);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser(Long userId){
        User user = getUserById(userId);
        user.delete();
    }

    /**
     * RefreshToken 검증
     */
    public void validateRefreshToken(Long userId, String token) {
        User user = getUserById(userId);
        if (user.getRefreshToken() == null ||
                !user.getRefreshToken().equals(token)) {
            throw new RuntimeException("유효하지 않은 RefreshToken입니다.");
        }
    }

    // == 공통 메서드 ==
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(
                        "존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND));
    }

    private void validateDuplicateUser(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new DuplicateUserException(
                    "이미 존재하는 아이디입니다.", HttpStatus.CONFLICT);
        }
    }
}