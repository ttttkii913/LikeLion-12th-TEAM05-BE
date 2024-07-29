package org.likelion.likelion_12th_team05.curation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.likelion.likelion_12th_team05.common.error.SuccessCode;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
import org.likelion.likelion_12th_team05.curation.api.dto.request.CurationUpdateReqDto;
import org.likelion.likelion_12th_team05.curation.api.dto.request.CurationSaveReqDto;
import org.likelion.likelion_12th_team05.curation.api.dto.response.CurationInfoResDto;
import org.likelion.likelion_12th_team05.curation.api.dto.response.CurationListResDto;
import org.likelion.likelion_12th_team05.curation.application.CurationService;
import org.likelion.likelion_12th_team05.location.application.LocationService;
import org.likelion.likelion_12th_team05.user.application.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/curation")
public class CurationController {

    private final CurationService curationService;
    private final LocationService locationService;
    private final UserService userService;

    @Operation(summary = "모든 사용자가 큐레이션 6개씩 조회", description = "모든 사용자가 산책로 지도 페이지에서 큐레이션을 한 페이지 당 6개씩 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<CurationListResDto> curationFindAll (
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort
    ) {
        Pageable pageable;
        if (sort.isEmpty()) {
            pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        } else {
            String[] sortParams = sort.split(",");
            Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
            pageable = PageRequest.of(page, size, sortOrder);
        }
        CurationListResDto curationListResDto = curationService.curationFindAll(pageable);
        return ApiResponseTemplate.successResponse(curationListResDto, SuccessCode.GET_SUCCESS);
    }

    @Operation(summary = "모든 사용자가 큐레이션 하나씩 조회", description = "모든 사용자가 산책로 지도 페이지에서 큐레이션 한 개씩 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
    })
    @GetMapping("/{curationId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<CurationInfoResDto> curationFindOne(@PathVariable("curationId") Long curationId) {
        CurationInfoResDto curationInfoResDto = curationService.curationFindOne(curationId);
        return ApiResponseTemplate.successResponse(curationInfoResDto, SuccessCode.GET_SUCCESS);
    }

    @Operation(summary = "인증된 사용자가 큐레이션 생성", description = "인증된 사용자가 산책로 생성 페이지에서 큐레이션을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // @Valid 추가 예정
    public ApiResponseTemplate<CurationInfoResDto> curationSave(@RequestBody CurationSaveReqDto curationSaveReqDto
                                                                , Principal principal) throws IOException {
        CurationInfoResDto curationInfoResDto = curationService.curationSave(curationSaveReqDto, principal);
        return ApiResponseTemplate.successResponse(curationInfoResDto, SuccessCode.CURATION_SAVE_SUCCESS);
    }

    @Operation(summary = "인증된 사용자가 큐레이션 수정", description = "인증된 사용자가 산책로 지도 페이지에서 큐레이션을 수정(큐레이션 제목, 설명)합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @PatchMapping("/{curationId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<CurationInfoResDto> curationUpdate(@PathVariable("curationId") Long curationId,
                                                                  @RequestBody CurationUpdateReqDto curationUpdateReqDto
                                                                    , Principal principal) {
        CurationInfoResDto curationInfoResDto = curationService.curationUpdate(curationId, curationUpdateReqDto, principal);
        return ApiResponseTemplate.successResponse(curationInfoResDto, SuccessCode.CURATION_UPDATE_SUCCESS);
    }

    @Operation(summary = "인증된 사용자가 큐레이션 삭제", description = "인증된 사용자가 큐레이션을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증이 필요합니다.")
    })
    @DeleteMapping("/{curationId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<SuccessCode> curationDelete(@PathVariable("curationId") Long curationId,
                                                           Principal principal) {
        curationService.curationDelete(curationId, principal);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.CURATION_DELETE_SUCCESS);
    }

    @Operation(summary = "모든 사용자가 큐레이션 검색", description = "모든 사용자가 산책로 지도 페이지에서 큐레이션을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
    })
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<CurationListResDto> searchCurations(@RequestParam String query) {
        CurationListResDto searchResults = curationService.searchCurations(query);
        return ApiResponseTemplate.successResponse(searchResults, SuccessCode.GET_SUCCESS);
    }

    @Operation(summary = "모든 사용자가 좋아요 순으로 정렬된 큐레이션 6개 조회", description = "모든 사용자가 랜딩페이지에서 좋아요 순으로 정렬된 6개의 큐레이션을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
    })
    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<CurationListResDto> findTop6ByOrderByLikeCountDesc() {
        CurationListResDto curationListResDto = curationService.findTop6ByOrderByLikeCountDesc();
        return ApiResponseTemplate.successResponse(curationListResDto, SuccessCode.GET_SUCCESS);
    }

    @Operation(summary = "모든 사용자가 최신순으로 정렬된 큐레이션 6개 조회", description = "모든 사용자가 랜딩페이지에서 최신순으로 정렬된 6개의 큐레이션을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
    })
    @GetMapping("/recent")
    public ApiResponseTemplate<CurationListResDto> findTop6ByOrderByCreateDateDesc() {
        CurationListResDto curationListResDto = curationService.findTop6ByOrderByCreateDateDesc();
        return ApiResponseTemplate.successResponse(curationListResDto, SuccessCode.GET_SUCCESS);
    }

    @Operation(summary = "인증된 사용자가 자신이 좋아요 누른 큐레이션 조회", description = "인증된 사용자가 마이페이지에서 자신이 좋아요 누른 큐레이션 목록을 6개씩 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
    })
    @GetMapping("/user/like")
    public ApiResponseTemplate<CurationListResDto> findCurationUserLikes(Principal principal,
                                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                                         @RequestParam(value = "size", defaultValue = "6") int size,
                                                                         @RequestParam(value = "sort", defaultValue = "id,asc") String sort
    ) {
        Pageable pageable;
        if (sort.isEmpty()) {
            pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        } else {
            String[] sortParams = sort.split(",");
            Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
            pageable = PageRequest.of(page, size, sortOrder);
        }
        CurationListResDto curationListResDto = curationService.findCurationUserLikes(pageable, principal);
        return ApiResponseTemplate.successResponse(curationListResDto, SuccessCode.GET_SUCCESS);
    }

}
