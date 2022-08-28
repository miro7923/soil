package august.soil.api;

import august.soil.domain.Member;
import august.soil.dto.*;
import august.soil.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 회원 가입
     * @param request
     * @return 가입된 회원의 DB index
     */
    @PostMapping("/members")
    public CreateMemberResponse saveMember(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member(request.getLoginId());
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    /**
     * 회원 정보 수정
     * @param id
     * @param request
     * @return 수정된 회원의 DB index, login id
     */
    @PatchMapping("/members/{member_id}")
    public UpdateMemberResponse updateMember(
            @PathVariable("member_id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getPassword());
        Member findMember = memberService.findMember(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getLoginId());
    }

    /**
     * 회원 한 명의 정보 조회
     * @param id
     * @return 회원 한 명의 정보
     */
    @GetMapping("/members/{member_id}")
    public MemberDto getMember(@PathVariable("member_id") Long id) {
        Member findMember = memberService.findMember(id);

        return new MemberDto(findMember.getLoginId());
    }

    /**
     * 회원 전체 목록 조회
     * @return 가입된 회원 전체의 login id
     */
    @GetMapping("/members")
    public MemberResult getMembers() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getLoginId()))
                .collect(Collectors.toList());

        return new MemberResult(findMembers.size(), collect);
    }
}
