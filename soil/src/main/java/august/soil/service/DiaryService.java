package august.soil.service;

import august.soil.domain.Diary;
import august.soil.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    // 변경할 일 없으니까 final
    private final DiaryRepository diaryRepository;

    /**
     * 일기 등록
     * @param diary
     * @return diary_id
     */
    @Transactional
    public Long upload(Diary diary) {
        diaryRepository.save(diary);
        return diary.getId();
    }

    /**
     * 일기 전체 조회
     * @return List<Diary>
     */
    public List<Diary> findDiaries() {
        return diaryRepository.findAll();
    }

    /**
     * 일기 하나 조회
     * @param id
     * @return Diary
     */
    public Diary findDiary(Long id) {
        return diaryRepository.findOne(id);
    }
}
