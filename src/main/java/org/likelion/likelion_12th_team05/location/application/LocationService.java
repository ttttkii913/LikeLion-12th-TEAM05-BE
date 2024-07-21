package org.likelion.likelion_12th_team05.location.application;

import lombok.RequiredArgsConstructor;
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
        Long id = Long.parseLong(principal.getName());
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION
                        , ErrorCode.USER_NOT_FOUND_EXCEPTION.getMessage()));

        Curation curation = curationRepository.findById(curationId).orElseThrow(
                () -> new NotFoundException(ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION
                , ErrorCode.CURATIONS_NOT_FOUND_EXCEPTION.getMessage()));

        String locationImage = s3Service.upload(multipartFile, "location");

        Location location = locationSaveReqDto.toEntity(locationImage, curation, user);
        locationRepository.save(location);

        return LocationInfoResDto.from(location);
    }

    // 인증된 사용자 - 고른 위치 조회
    @Transactional
    public LocationListResDto locationFindAll(Principal principal) {
        Long id = Long.parseLong(principal.getName());

        List<Location> locations = locationRepository.findByUserId(id);

        List<LocationInfoResDto> locationInfoResDtoList = locations.stream()
                .map(LocationInfoResDto::from)
                .toList();
        return LocationListResDto.from(locationInfoResDtoList);
    }

    // 인증된 사용자 - 위치 수정 - 이름, 설명
    @Transactional
    public LocationInfoResDto locationUpdate(Long locationId, LocationUpdateReqDto locationUpdateReqDto
                                            , MultipartFile locationImage, Principal principal) throws IOException {
        Long id = Long.parseLong(principal.getName());
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION
                        , ErrorCode.USER_NOT_FOUND_EXCEPTION.getMessage()));

        Location location = locationRepository.findById(locationId)
                .orElseThrow( ()-> new NotFoundException(ErrorCode.LOCATIONS_NOT_FOUND_EXCEPTION
                        , ErrorCode.LOCATIONS_NOT_FOUND_EXCEPTION.getMessage()));
        location.update(locationUpdateReqDto, user);

        // 수정 권한 확인
        Long LoginId = Long.parseLong(principal.getName());

        if (!id.equals(LoginId)) {
            throw new NotFoundException(ErrorCode.NO_AUTHORIZATION_EXCEPTION
                    , ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }

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
        Location location = locationRepository.findById(locationId)
                .orElseThrow( ()-> new NotFoundException(ErrorCode.LOCATIONS_NOT_FOUND_EXCEPTION
                        , ErrorCode.LOCATIONS_NOT_FOUND_EXCEPTION.getMessage()));

        // 삭제 권한 확인
        Long id = Long.parseLong(principal.getName());
        Long LoginId = Long.parseLong(principal.getName());

        if (!id.equals(LoginId)) {
            throw new NotFoundException(ErrorCode.NO_AUTHORIZATION_EXCEPTION
                    , ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }

        locationRepository.delete(location);
    }
}
