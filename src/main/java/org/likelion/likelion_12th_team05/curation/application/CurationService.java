package org.likelion.likelion_12th_team05.curation.application;

import lombok.RequiredArgsConstructor;
import org.likelion.likelion_12th_team05.common.EntityFinder;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.NotFoundException;
import org.likelion.likelion_12th_team05.curation.api.dto.request.CurationUpdateReqDto;
import org.likelion.likelion_12th_team05.curation.api.dto.request.CurationSaveReqDto;
import org.likelion.likelion_12th_team05.curation.api.dto.response.CurationInfoResDto;
import org.likelion.likelion_12th_team05.curation.api.dto.response.CurationListResDto;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.curation.domain.repository.CurationRepository;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.likelion.likelion_12th_team05.user.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurationService {
    private final CurationRepository curationRepository;
    private final UserRepository userRepository;

    // 인증된 사용자 - 큐레이션 생성
    @Transactional
    public CurationInfoResDto curationSave(CurationSaveReqDto curationSaveReqDto, Principal principal) {
        String email = principal.getName();
        User user = getUserByEmail(email);

        Curation curation = curationSaveReqDto.toEntity(user);
        curationRepository.save(curation);

        return CurationInfoResDto.from(curation);
    }

    // 인증된 사용자 - 큐레이션 수정
    @Transactional
    public CurationInfoResDto curationUpdate(Long curationId, CurationUpdateReqDto curationUpdateReqDto, Principal principal) {
        String email = principal.getName();
        User user = getUserByEmail(email);

        Curation curation = getCurationById(curationId);

        // 큐레이션 작성자 이메일과 일치하는지 확인 - 수정 권한 확인
        String CurationUserEmail = curation.getUser().getEmail();
        if (!email.equals(CurationUserEmail)) {
            throw new NotFoundException(ErrorCode.NO_AUTHORIZATION_EXCEPTION
                    , ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }

        curation.update(curationUpdateReqDto, user);
        curationRepository.save(curation);
        return CurationInfoResDto.from(curation);
    }

    // 인증된 사용자 - 큐레이션 삭제
    @Transactional
    public void curationDelete(Long curationId, Principal principal) {
        Curation curation = getCurationById(curationId);

        // 큐레이션 작성자 이메일과 일치하는지 확인 - 삭제 권한 확인
        String email = principal.getName();
        String CurationUserEmail = curation.getUser().getEmail();

        if (!email.equals(CurationUserEmail)) {
            throw new NotFoundException(ErrorCode.NO_AUTHORIZATION_EXCEPTION
                    , ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }
        curationRepository.delete(curation);
    }

    // 모든 사용자 - 큐레이션 검색
    @Transactional
    public CurationListResDto searchCurations(String query) {
        List<Curation> curations = curationRepository.findByNameContaining(query);
        List<CurationInfoResDto> curationInfoResDtoList = curations.stream()
                .map(CurationInfoResDto::from)
                .toList();
        return CurationListResDto.from(curationInfoResDtoList);
    }

    // 모든 사용자 - 큐레이션 6개 페이지네이션
    @Transactional
    public CurationListResDto curationFindAll(Pageable pageable) {
        Page<Curation> curations = curationRepository.findAll(pageable);

        List<CurationInfoResDto> curationInfoResDtoList = curations.stream()
                .map(CurationInfoResDto::from)
                .toList();
        return CurationListResDto.from(curationInfoResDtoList);
    }

    // 모든 사용자 - 산책로 지도 페이지 - 큐레이션 한 개씩 조회
    @Transactional
    public CurationInfoResDto curationFindOne(@PathVariable("curationId") Long curationId) {
        Curation curation = getCurationById(curationId);
        return CurationInfoResDto.from(curation);
    }

    // 모든 사용자 - 랜딩 페이지 - 큐레이션 좋아요 많은 순으로 6개만 조회
    @Transactional
    public CurationListResDto findTop6ByOrderByLikeCountDesc() {
        List<Curation> curations = curationRepository.findTop6ByOrderByLikeCountDesc();
        List<CurationInfoResDto> curationInfoResDtoList = curations.stream()
                .map(CurationInfoResDto::from)
                .toList();
        return CurationListResDto.from(curationInfoResDtoList);
    }

    // 모든 사용자 - 랜딩페이지 - 큐레이션 최신(createDate desc) 순으로 6개만 조회
    @Transactional
    public CurationListResDto findTop6ByOrderByCreateDateDesc() {
        List<Curation> curations = curationRepository.findTop6ByOrderByCreateDateDesc();
        List<CurationInfoResDto> curationInfoResDtoList = curations.stream()
                .map(CurationInfoResDto::from)
                .toList();
        return CurationListResDto.from(curationInfoResDtoList);
    }

    // 반복되는 예외 반환 메서드 추출 => 공통 예외 처리로 일관성 높이기 위함 => entityfinder로 중앙 관리
    private Curation getCurationById(Long curationId) {
        return EntityFinder.findByIdOrThrow(curationRepository.findById(curationId)
                , ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION);
    }

    private User getUserByEmail(String email) {
        return EntityFinder.findByEmailOrThrow(userRepository.findByEmail(email)
                , ErrorCode.USER_NOT_FOUND_EXCEPTION);
    }
}
