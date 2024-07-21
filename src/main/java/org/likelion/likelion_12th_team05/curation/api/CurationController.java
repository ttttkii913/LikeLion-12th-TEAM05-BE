package org.likelion.likelion_12th_team05.curation.api;

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

    // 큐레이션 지도 페이지 - 모든 사용자가 큐레이션 6개씩 조회 가능
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

    // 큐레이션 생성 페이지 - 인증된 사용자가 큐레이션 생성 가능
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // @Valid 추가 예정
    public ApiResponseTemplate<CurationInfoResDto> curationSave(@RequestBody CurationSaveReqDto curationSaveReqDto
                                                                , Principal principal) throws IOException {
        CurationInfoResDto curationInfoResDto = curationService.curationSave(curationSaveReqDto, principal);
        return ApiResponseTemplate.successResponse(curationInfoResDto, SuccessCode.CURATION_SAVE_SUCCESS);
    }

    // 큐레이션 지도 페이지 - 인증된 사용자가 큐레이션 이름, content 수정, 삭제 가능
    @PatchMapping("/{curationId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<CurationInfoResDto> curationUpdate(@PathVariable("curationId") Long curationId,
                                                                  @RequestBody CurationUpdateReqDto curationUpdateReqDto
                                                                    , Principal principal) {
        CurationInfoResDto curationInfoResDto = curationService.curationUpdate(curationId, curationUpdateReqDto, principal);
        return ApiResponseTemplate.successResponse(curationInfoResDto, SuccessCode.CURATION_UPDATE_SUCCESS);
    }

    // 인증된 사용자가 큐레이션 삭제
    @DeleteMapping("/{curationId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<SuccessCode> curationDelete(@PathVariable("curationId") Long curationId,
                                                           Principal principal) {
        curationService.curationDelete(curationId, principal);
        return ApiResponseTemplate.successWithNoContent(SuccessCode.GET_SUCCESS);
    }

    // 큐레이션 지도 페이지 - 모든 사용자가 큐레이션 검색 가능(query="서울" 이런 식으로)
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseTemplate<CurationListResDto> searchCurations(@RequestParam String query) {
        CurationListResDto searchResults = curationService.searchCurations(query);
        return ApiResponseTemplate.successResponse(searchResults, SuccessCode.GET_SUCCESS);
    }

    // - 중, 조회-
    // 랜딩 페이지 - 게시글을 많이 작성한 5명의 큐레이터 조회 가능

}
