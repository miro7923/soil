package august.soil.api;

import august.soil.domain.Diary;
import august.soil.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryApiController {

    private final DiaryService diaryService;

    /**
     * 회원 한 명의 일기 목록을 응답
     * @param id
     * @return id에 해당하는 회원의 일기 전체 목록
     */
    @GetMapping("/diaries/{id}")
    public List<Diary> diaries(@PathVariable Long id) {
        return diaryService.findDiaries(id);
    }
}
