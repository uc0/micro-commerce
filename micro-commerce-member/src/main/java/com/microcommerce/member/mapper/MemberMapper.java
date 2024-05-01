package com.microcommerce.member.mapper;

import com.microcommerce.member.domain.dto.req.UpdateMemberReqDto;
import com.microcommerce.member.domain.vo.UpdateMemberVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    UpdateMemberVo toVo(long userId, UpdateMemberReqDto dto);

}
