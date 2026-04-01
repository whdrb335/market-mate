package com.example.market_mate.domain.user.api;

import com.example.market_mate.common.Result;
import com.example.market_mate.common.jwt.JwtTokenProvider;
import com.example.market_mate.domain.user.dto.UserDTO;
import com.example.market_mate.domain.user.entity.User;
import com.example.market_mate.domain.user.request.*;
import com.example.market_mate.domain.user.response.LoginResponse;
import com.example.market_mate.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User API", description = "회원가입 · 로그인 · 인증 · 회원 정보 관리 API")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public Result<UserDTO> signup(@Validated @RequestBody SignRequest request) {
        User user = userService.createUser(
                request.getLoginId(), request.getLoginPw(),
                request.getName(), request.getPhone(),
                request.getStoreName(), request.getMarketName()
        );
        return new Result<>(new UserDTO(user));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        User user = userService.login(request.getLoginId(), request.getLoginPw());

        String accessToken = jwtTokenProvider.createAccessToken(
                user.getId(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        userService.updateRefreshToken(user.getId(), refreshToken);

        return new Result<>(new LoginResponse(
                user.getId(), user.getLoginId(),
                user.getRole().name(), accessToken, refreshToken));
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping("/myInfo")
    public Result<UserDTO> myInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return new Result<>(new UserDTO(userService.getMyInfo(userId)));
    }

    @Operation(summary = "비밀번호 변경")
    @PatchMapping("/changePasswd")
    public Result<String> changePasswd(
            @Validated @RequestBody ChangePasswdRequest request,
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        userService.changePasswd(userId, request.getOldPasswd(), request.getNewPasswd());
        return new Result<>("비밀번호가 변경되었습니다.");
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh")
    public Result<LoginResponse> refresh(@RequestBody RefreshTokenRequest request) {
        String clientRefreshToken = request.getRefreshToken();
        Long userId = Long.parseLong(
                jwtTokenProvider.parseClaims(clientRefreshToken).getSubject());

        User user = userService.getMyInfo(userId);
        userService.validateRefreshToken(userId, clientRefreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(
                userId, user.getRole().name());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);
        userService.updateRefreshToken(userId, newRefreshToken);

        return new Result<>(new LoginResponse(
                userId, user.getLoginId(),
                user.getRole().name(), newAccessToken, newRefreshToken));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateRefreshToken(userId, null);
        return new Result<>("로그아웃 되었습니다.");
    }

    @Operation(summary = "회원탈퇴")
    @DeleteMapping("/delete")
    public Result<String> delete(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.deleteUser(userId);
        return new Result<>("탈퇴되었습니다.");
    }
}