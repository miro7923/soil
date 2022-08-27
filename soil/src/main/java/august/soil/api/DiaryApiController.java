package august.soil.api;

import august.soil.domain.Category;
import august.soil.domain.Diary;
import august.soil.domain.Member;
import august.soil.dto.DiaryDto;
import august.soil.dto.Result;
import august.soil.service.CategoryService;
import august.soil.service.DiaryService;
import august.soil.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DiaryApiController {

    private final DiaryService diaryService;
    private final MemberService memberService;
    private final CategoryService categoryService;

    /**
     * 회원 한 명의 일기 목록을 응답
     * @param id
     * @return id에 해당하는 회원의 일기 전체 목록
     */
    @GetMapping("/diaries/{id}")
    public Result diaries(@PathVariable Long id) {
        List<Diary> findDiaries = diaryService.findDiaries(id);
        Member findMember = memberService.findMember(id);
        Category findCategory = categoryService.findCategory(1L);

        List<DiaryDto> collect = findDiaries.stream()
                .map(d -> new DiaryDto(
                        d.getCategory().getName(),
                        d.getTitle(),
                        d.getContent(),
                        d.getDate(),
                        d.getPhoto(),
                        d.getPrice()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }
}
