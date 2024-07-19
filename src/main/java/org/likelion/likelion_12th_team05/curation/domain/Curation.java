package org.likelion.likelion_12th_team05.curation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.likelion.likelion_12th_team05.common.BaseTimeEntity;
import org.likelion.likelion_12th_team05.curation.api.dto.request.CurationUpdateReqDto;
import org.likelion.likelion_12th_team05.location.domain.Location;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curation_id")
    private Long id;

    @Column(name = "curation_name", nullable = false)
    private String name;

    @Column(name = "curation_content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "curation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations = new ArrayList<>();

    //@OneToMany(mappedBy = "curation", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<User> users = new ArrayList<>();

    @Builder
    public Curation(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public void update(CurationUpdateReqDto curationUpdateReqDto) {
        this.name = curationUpdateReqDto.name();
        this.content = curationUpdateReqDto.content();
    }
}
