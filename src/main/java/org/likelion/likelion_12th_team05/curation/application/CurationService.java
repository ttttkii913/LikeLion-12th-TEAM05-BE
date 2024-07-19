package org.likelion.likelion_12th_team05.curation.application;

import lombok.RequiredArgsConstructor;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.NotFoundException;
import org.likelion.likelion_12th_team05.curation.api.dto.request.CurationUpdateReqDto;
import org.likelion.likelion_12th_team05.curation.api.dto.request.CurationSaveReqDto;
import org.likelion.likelion_12th_team05.curation.api.dto.response.CurationInfoResDto;
import org.likelion.likelion_12th_team05.curation.api.dto.response.CurationListResDto;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.curation.domain.repository.CurationRepository;
import org.likelion.likelion_12th_team05.location.domain.Location;
import org.likelion.likelion_12th_team05.location.domain.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurationService {
    private final CurationRepository curationRepository;
    private final LocationRepository locationRepository;

    // 큐레이션 생성
    @Transactional
    public CurationInfoResDto curationSave(CurationSaveReqDto curationSaveReqDto)  {

        Curation curation = curationSaveReqDto.toEntity();
        curationRepository.save(curation);

        return CurationInfoResDto.from(curation);
    }

    // 큐레이션 6개 페이지네이션
    @Transactional
    public CurationListResDto curationFindAll(Pageable pageable) {
        Page<Curation> curations = curationRepository.findAll(pageable);

        List<CurationInfoResDto> curationInfoResDtoList = curations.stream()
                .map(CurationInfoResDto::from)
                .toList();
        return CurationListResDto.from(curationInfoResDtoList);
    }

    // 큐레이션 수정
    @Transactional
    public CurationInfoResDto curationUpdate(Long curationId, CurationUpdateReqDto curationUpdateReqDto) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 큐레이션이 없습니다. id=" + curationId));

        curation.update(curationUpdateReqDto);
        curationRepository.save(curation);
        return CurationInfoResDto.from(curation);
    }

    // 큐레이션 검색 searchCurations
    @Transactional
    public CurationListResDto searchCurations(String query) {
        List<Curation> curations = curationRepository.findByNameContaining(query);
        List<CurationInfoResDto> curationInfoResDtoList = curations.stream()
                .map(CurationInfoResDto::from)
                .toList();
        return CurationListResDto.from(curationInfoResDtoList);
    }

    @Transactional
    public void curationDelete(Long curationId) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 큐레이션이 없습니다. id=" + curationId));

        curationRepository.delete(curation);
    }
}
