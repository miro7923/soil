package august.soil.web.api;

import august.soil.domain.Category;
import august.soil.domain.Diary;
import august.soil.domain.Member;
import august.soil.web.dto.DiaryDto;
import august.soil.web.dto.DiariesResult;
import august.soil.service.CategoryService;
import august.soil.service.DiaryService;
import august.soil.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DiaryApiController {

    private final DiaryService diaryService;
    private final MemberService memberService;
    private final CategoryService categoryService;

    /**
     * 회원 한 명의 일기 목록을 응답
     * @param member_id
     * @return id에 해당하는 회원의 일기 전체 목록
     */
    @GetMapping("/diaries/{member_id}")
    public DiariesResult diaries(@PathVariable Long member_id) {
        List<Diary> findDiaries = diaryService.findDiaries(member_id);
        Member findMember = memberService.findMember(member_id);
        Category findCategory = categoryService.findCategory(1L);

        List<DiaryDto> collect = findDiaries.stream()
                .map(d -> new DiaryDto(
                        d.getCategory().getName(),
                        d.getTitle(),
                        d.getContent(),
                        d.getDate(),
//                        d.getPhoto(),
                        d.getPrice()))
                .collect(Collectors.toList());

        return new DiariesResult(collect.size(), collect);
    }
}
