package org.likelion.likelion_12th_team05.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.likelion.likelion_12th_team05.curation.domain.Curation;
import org.likelion.likelion_12th_team05.location.api.dto.request.LocationUpdateReqDto;
import org.likelion.likelion_12th_team05.user.domain.User;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "location_name", nullable = false)
    private String name;

    @Column(name = "location_description", nullable = false)
    private String description;

    @JsonIgnore
    @Column(name = "location_image")
    private String locationImage;

    @JsonIgnore
    @Column(name = "location_address", nullable = false)
    private String address;

    @Column(name = "location_logitude")
    private Double longitude;

    @Column(name = "location_latitude")
    private Double latitude;

    @ManyToOne
    @JoinColumn(name = "curation_id")
    private Curation curation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Location(String name, String description, String address, String locationImage, Curation curation, User user, Double longitude, Double latitude) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.locationImage = locationImage;
        this.curation = curation;
        this.user = user;
        this.longitude = longitude;
        this.latitude = latitude;
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
