package org.likelion.likelion_12th_team05.location.api;

import lombok.RequiredArgsConstructor;

import org.likelion.likelion_12th_team05.common.error.SuccessCode;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
import org.likelion.likelion_12th_team05.curation.application.CurationService;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.location.api.dto.request.LocationSaveReqDto;
import org.likelion.likelion_12th_team05.location.api.dto.request.LocationUpdateReqDto;
import org.likelion.likelion_12th_team05.location.api.dto.response.LocationInfoResDto;
import org.likelion.likelion_12th_team05.location.api.dto.response.LocationListResDto;
import org.likelion.likelion_12th_team05.location.application.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;
    private final CurationService curationService;

    // 내가 지도에서 고른 위치 모두 보기
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<LocationListResDto> locationFindAll() {
        LocationListResDto locationListResDto = locationService.locationFindAll();
        return ApiResponseTemplate.successResponse(locationListResDto, SuccessCode.GET_SUCCESS);
    }

    // 위치 생성 - 위치 이름 + 산책로 설명 + 사진 저장
    @PostMapping(value = "/{curationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<LocationInfoResDto> locationSave(@RequestPart("location") LocationSaveReqDto locationSaveReqDto,
                                                                @RequestPart("locationImage") MultipartFile locationImage,
                                                                @PathVariable("curationId") Long curationId) throws IOException {
        LocationInfoResDto locationInfoResDto = locationService.locationSave(locationSaveReqDto, locationImage, curationId);
        return ApiResponseTemplate.successResponse(locationInfoResDto, SuccessCode.LOCATION_SAVE_SUCCESS);
    }

    // 산책로 수정 - 산책로 설명 + 사진 수정
    @PatchMapping(value = "/{locationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<LocationInfoResDto> locationUpdate(@PathVariable("locationId") Long locationId,
                                                                  @RequestPart("location") LocationUpdateReqDto locationUpdateReqDto,
                                                                  @RequestPart("locationImage") MultipartFile locationImage) throws IOException {
        LocationInfoResDto locationInfoResDto = locationService.locationUpdate(locationId, locationUpdateReqDto, locationImage);
        return ApiResponseTemplate.successResponse(locationInfoResDto, SuccessCode.LOCATION_UPDATE_SUCCESS);
    }

    // 산책로 삭제 - 아예 장소를 삭제
    @DeleteMapping("/{locationId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<String> locationDelete(@PathVariable("locationId") Long locationId) {
        locationService.locationDelete(locationId);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.LOCATION_DELETE_SUCCESS);
    }

}
