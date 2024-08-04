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
        // 큐레이션 생성 시 사용자마다 큐레이션 개수가 하나 증가하도록 -> 랜딩 페이지에서 큐레이션 가장 많이 작성한 큐레이터 조회하기 위함 -> 삭제 시에도 반영
        user.setCurationCount(user.getCurationCount() + 1);
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
        User user = getUserByEmail(email);
        String CurationUserEmail = curation.getUser().getEmail();

        if (!email.equals(CurationUserEmail)) {
            throw new NotFoundException(ErrorCode.NO_AUTHORIZATION_EXCEPTION
                    , ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }
        // 큐레이션 삭제 시에 사용자의 큐레이션 개수가 하나 줄어들도록
        user.setCurationCount(user.getCurationCount() - 1);
        curationRepository.delete(curation);
    }

    // 모든 사용자 - 큐레이션 검색
    @Transactional
    public CurationListResDto searchCurations(String query, Pageable pageable) {
        Page<Curation> curations = curationRepository.findByNameContaining(query, pageable);
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

    // 모든 사용자 - 랜딩 페이지 - 큐레이션 좋아요 많은 순으로 페이지네이션
    @Transactional
    public CurationListResDto findAllByOrderByLikeCountDesc(Pageable pageable) {
        Page<Curation> curations = curationRepository.findAllByOrderByLikeCountDesc(pageable);
        List<CurationInfoResDto> curationInfoResDtoList = curations.stream()
                .map(CurationInfoResDto::from)
                .toList();
        return CurationListResDto.from(curationInfoResDtoList);
    }

    // 모든 사용자 - 랜딩페이지 - 큐레이션 최신(createDate desc) 순으로 6개씩 페이지네이션
    @Transactional
    public CurationListResDto findAllByOrderByCreateDateDesc(Pageable pageable) {
        Page<Curation> curations = curationRepository.findAllByOrderByCreateDateDesc(pageable);
        List<CurationInfoResDto> curationInfoResDtoList = curations.stream()
                .map(CurationInfoResDto::from)
                .toList();
        return CurationListResDto.from(curationInfoResDtoList);
    }

    // 인증된 사용자 - 마이페이지 - 자신이 좋아요 누른 큐레이션 목록(페이지네이션 6개) 조회
    @Transactional
    public CurationListResDto findCurationUserLikes(Pageable pageable, Principal principal) {
        String email = principal.getName();
        User user = getUserByEmail(email);

        // 좋아요 누른 큐레이션이 없다면 "아직 좋아요를 누르지 않았습니다" 던져주기
        if (user.getLikes().isEmpty())
            throw new NotFoundException(ErrorCode.NO_USER_LIKE_CURATIONS_EXCEPTION,
                    ErrorCode.NO_USER_LIKE_CURATIONS_EXCEPTION.getMessage());

        Page<Curation> curations = curationRepository.findUserLikes(user, pageable);
        List<CurationInfoResDto> curationInfoResDtoList = curations.stream()
                .map(CurationInfoResDto::from)
                .toList();
        return CurationListResDto.from(curationInfoResDtoList);
    }

    @Transactional
    public CurationListResDto findCommentCount(Pageable pageable) {
        Page<Curation> curations = curationRepository.findAllByOrderByCommentCountDesc(pageable);
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
