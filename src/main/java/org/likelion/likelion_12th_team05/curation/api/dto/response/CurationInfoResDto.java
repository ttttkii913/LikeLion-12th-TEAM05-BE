package org.likelion.likelion_12th_team05.curation.api.dto.response;

import lombok.Builder;
import org.likelion.likelion_12th_team05.comment.api.response.CommentInfoResDto;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.location.api.dto.response.LocationInfoResDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record CurationInfoResDto(
        Long id,
        String name,
        String content,
        LocalDateTime createDate,
        Integer likeCount,
        Integer commentCount,
        List<LocationInfoResDto> locations,
        List<CommentInfoResDto> comments

) {
    public static CurationInfoResDto from(Curation curation) {
        return CurationInfoResDto.builder()
                .id(curation.getId())
                .name(curation.getName())
                .content(curation.getContent())
                .createDate(curation.getCreateDate())
                .commentCount(curation.getCommentCount())
                .likeCount(curation.getLikeCount())
                .locations(curation.getLocations().stream()
                        .map(LocationInfoResDto::from)
                        .collect(Collectors.toList()))
                .comments(curation.getComments().stream().map(CommentInfoResDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
