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
import org.likelion.likelion_12th_team05.user.domain.User;
import org.likelion.likelion_12th_team05.user.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION
                        , ErrorCode.USER_NOT_FOUND_EXCEPTION.getMessage()));
        Curation curation = curationSaveReqDto.toEntity(user);
        curationRepository.save(curation);

        return CurationInfoResDto.from(curation);
    }

    // 인증된 사용자 - 큐레이션 수정
    @Transactional
    public CurationInfoResDto curationUpdate(Long curationId, CurationUpdateReqDto curationUpdateReqDto, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION
                        , ErrorCode.USER_NOT_FOUND_EXCEPTION.getMessage()));

        Curation curation = curationRepository.findById(curationId).orElseThrow(
                () -> new NotFoundException(ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION
                                , ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION.getMessage()));

        // 수정 권한 확인 -- 토큰 발급의 주체가 email이기에 email로 사용자 확인을 하였으나
        // userid로 판단 하는 것이 더 좋지 않을까 생각함. 일단 crud와 로그인 연결 테스트를 위해 email로 테스트 함 -- 상의 필요
        String LoginEmail = principal.getName();
        if (!email.equals(LoginEmail)) {
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
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 큐레이션이 없습니다. id=" + curationId));

        // 삭제 권한 확인
        Long id = Long.parseLong(principal.getName());
        Long LoginId = Long.parseLong(principal.getName());

        if (!id.equals(LoginId)) {
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

    // 모든 사용자 - 큐레이션 좋아요 많은 순으로 6개만 조회
    @Transactional
    public CurationListResDto findTop6ByOrderByLikeCountDesc() {
        List<Curation> curations = curationRepository.findTop6ByOrderByLikeCountDesc();
        List<CurationInfoResDto> curationInfoResDtoList = curations.stream()
                .map(CurationInfoResDto::from)
                .toList();
        return CurationListResDto.from(curationInfoResDtoList);
    }

}
