package org.likelion.likelion_12th_team05.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.location.api.dto.request.LocationUpdateReqDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_name", nullable = false)
    private String name;

    @Column(name = "location_description", nullable = false)
    private String description;

    @JsonIgnore
    @Column(name = "location_image", nullable = false)
    private String locationImage;

    @JsonIgnore
    @Column(name = "location_address", nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "curtaion_id")
    private Curation curation;

    @Builder
    public Location(String name, String description, String address, String locationImage, Curation curation) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.locationImage = locationImage;
        this.curation = curation;
    }

    public void update(LocationUpdateReqDto locationUpdateReqDto) {
        this.name = locationUpdateReqDto.name();
        this.description = locationUpdateReqDto.description();
        this.address = locationUpdateReqDto.address();
    }

    public void updateImage(String locationImage) {
        this.locationImage = locationImage;
    }

}
