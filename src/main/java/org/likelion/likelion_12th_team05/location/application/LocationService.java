package org.likelion.likelion_12th_team05.location.application;

import lombok.RequiredArgsConstructor;
import org.likelion.likelion_12th_team05.common.EntityFinder;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.NotFoundException;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.curation.domain.repository.CurationRepository;
import org.likelion.likelion_12th_team05.location.api.dto.request.LocationSaveReqDto;
import org.likelion.likelion_12th_team05.location.api.dto.request.LocationUpdateReqDto;
import org.likelion.likelion_12th_team05.location.api.dto.response.LocationInfoResDto;
import org.likelion.likelion_12th_team05.location.api.dto.response.LocationListResDto;
import org.likelion.likelion_12th_team05.location.domain.Location;
import org.likelion.likelion_12th_team05.location.domain.repository.LocationRepository;
import org.likelion.likelion_12th_team05.s3.S3Service;
import org.likelion.likelion_12th_team05.user.domain.User;
import org.likelion.likelion_12th_team05.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final CurationRepository curationRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;

    // 인증된 사용자 - 위치 저장
    @Transactional
    public LocationInfoResDto locationSave(LocationSaveReqDto locationSaveReqDto, MultipartFile multipartFile
                                                    , Long curationId, Principal principal) throws IOException {
        String email = principal.getName();
        User user = getUserByEmail(email);

        Curation curation = getCurationById(curationId);

        String CurationUserEmail = curation.getUser().getEmail();
        if (!email.equals(CurationUserEmail)) {
            throw new NotFoundException(ErrorCode.NO_AUTHORIZATION_EXCEPTION
                    , ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }

        String locationImage = s3Service.upload(multipartFile, "location");

        Location location = locationSaveReqDto.toEntity(locationImage, curation, user);
        locationRepository.save(location);

        return LocationInfoResDto.from(location);
    }

    // 인증된 사용자 - 고른 위치 조회
    @Transactional
    public LocationListResDto locationFindAll(Principal principal) {
        String email = principal.getName();

        List<Location> locations = locationRepository.findByUserEmail(email);

        List<LocationInfoResDto> locationInfoResDtoList = locations.stream()
                .map(LocationInfoResDto::from)
                .toList();
        return LocationListResDto.from(locationInfoResDtoList);
    }

    // 인증된 사용자 - 위치 수정 - 이름, 설명
    @Transactional
    public LocationInfoResDto locationUpdate(Long locationId, LocationUpdateReqDto locationUpdateReqDto
                                            , MultipartFile locationImage, Principal principal) throws IOException {
        Location location = getLocationById(locationId);

        // 수정 권한 확인
        String email = principal.getName();
        String LocationUserEmail = location.getUser().getEmail();

        if (!email.equals(LocationUserEmail)) {
            throw new NotFoundException(ErrorCode.NO_AUTHORIZATION_EXCEPTION
                    , ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }

        location.update(locationUpdateReqDto);

        // 이미지가 있을 경우 S3에 업로드하고 URL 업데이트
        if (locationImage != null && !locationImage.isEmpty()) {
            String imageUrl = s3Service.upload(locationImage, "location");
            location.updateImage(imageUrl);
        }
        locationRepository.save(location);
        return LocationInfoResDto.from(location);
    }

    // 인증된 사용자 - 위치 삭제
    @Transactional
    public void locationDelete(Long locationId, Principal principal) {
        Location location = getLocationById(locationId);
        // 삭제 권한 확인
        String email = principal.getName();
        String LocationUserEmail = location.getUser().getEmail();

        if (!email.equals(LocationUserEmail)) {
            throw new NotFoundException(ErrorCode.NO_AUTHORIZATION_EXCEPTION
                    , ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }

        locationRepository.delete(location);
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

    private Location getLocationById(Long locationId) {
        return EntityFinder.findByIdOrThrow(locationRepository.findById(locationId)
                , ErrorCode.LOCATIONS_NOT_FOUND_EXCEPTION);
    }
}
