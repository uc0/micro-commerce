package com.microcommerce.member.presentation;

import com.microcommerce.member.application.MemberService;
import com.microcommerce.member.domain.dto.req.SignInReqDto;
import com.microcommerce.member.domain.dto.req.SignUpReqDto;
import com.microcommerce.member.domain.dto.req.UpdateMemberReqDto;
import com.microcommerce.member.domain.dto.res.ProfileResDto;
import com.microcommerce.member.domain.dto.res.SignInResDto;
import com.microcommerce.member.domain.dto.res.SignUpResDto;
import com.microcommerce.member.mapper.MemberMapper;
import com.microcommerce.member.util.HeaderCheckUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    private final MemberMapper memberMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/public-api/v1/members/sign-up")
    public SignUpResDto signUp(@RequestBody final SignUpReqDto body) {
        return memberService.signUp(body);
    }

    @PostMapping("/public-api/v1/members/sign-in")
    public SignInResDto signIn(@RequestBody final SignInReqDto body) {
        return memberService.signIn(body);
    }

    @GetMapping("/api/v1/members/{userId}")
    public ProfileResDto getProfile(@RequestHeader final HttpHeaders header, @PathVariable("userId") final long userId) {
        HeaderCheckUtil.checkUserId(header, userId);
        return memberService.getProfile(userId);
    }

    @PutMapping("/api/v1/members/{userId}")
    public void updateProfile(@RequestHeader final HttpHeaders header,
                              @PathVariable("userId") final long userId,
                              @RequestBody final UpdateMemberReqDto body) {
        HeaderCheckUtil.checkUserId(header, userId);
        memberService.updateProfile(memberMapper.toVo(userId, body));
    }

}
