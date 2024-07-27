package org.likelion.likelion_12th_team05.location.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.likelion.likelion_12th_team05.common.error.SuccessCode;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
import org.likelion.likelion_12th_team05.curation.application.CurationService;
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
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;
    private final CurationService curationService;

    @Operation(summary = "인증된 사용자가 고른 위치 조회", description = "인증된 사용자가 고른 위치를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<LocationListResDto> locationFindAll(Principal principal) {
        LocationListResDto locationListResDto = locationService.locationFindAll(principal);
        return ApiResponseTemplate.successResponse(locationListResDto, SuccessCode.GET_SUCCESS);
    }

    @Operation(summary = "인증된 사용자가 위치 생성", description = "인증된 사용자가 위치를 생성(큐레이션 id, 위치 이름, 이미지)합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @PostMapping(value = "/{curationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<LocationInfoResDto> locationSave(@RequestPart("location") LocationSaveReqDto locationSaveReqDto,
                                                                @RequestParam(value = "locationImage", required=false) MultipartFile locationImage,
                                                                @PathVariable("curationId") Long curationId,
                                                                Principal principal) throws IOException {
        LocationInfoResDto locationInfoResDto = locationService.locationSave(locationSaveReqDto, locationImage, curationId, principal);
        return ApiResponseTemplate.successResponse(locationInfoResDto, SuccessCode.LOCATION_SAVE_SUCCESS);
    }

    @Operation(summary = "인증된 사용자가 위치 수정", description = "인증된 사용자가 위치를 수정(위치 이름, 이미지(선택), 주소)합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @PatchMapping(value = "/{locationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<LocationInfoResDto> locationUpdate(@PathVariable("locationId") Long locationId,
                                                                  @RequestPart("location") LocationUpdateReqDto locationUpdateReqDto,
                                                                  @RequestParam(value = "locationImage", required = false) MultipartFile locationImage,
                                                                  Principal principal) throws IOException {
        LocationInfoResDto locationInfoResDto = locationService.locationUpdate(locationId, locationUpdateReqDto, locationImage, principal);
        return ApiResponseTemplate.successResponse(locationInfoResDto, SuccessCode.LOCATION_UPDATE_SUCCESS);
    }

    @Operation(summary = "인증된 사용자가 위치 삭제", description = "인증된 사용자가 위치를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @DeleteMapping("/{locationId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<String> locationDelete(@PathVariable("locationId") Long locationId, Principal principal) {
        locationService.locationDelete(locationId, principal);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.LOCATION_DELETE_SUCCESS);
    }

}
