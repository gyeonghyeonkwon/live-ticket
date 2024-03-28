package com.ll.ticket.domain.admin.dto;

import com.ll.ticket.global.enums.ConcertCategory;
import com.ll.ticket.global.enums.ConcertStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RegisterConcertDto {
    @NotBlank(message = "제목을 입력하세요")
    private String name;

    @NotBlank(message = "콘서트명을 입력하세요")
    private String concertNameKr;

    @NotBlank(message = "콘서트 영문명을 입력하세요")
    private String concertNameEng;

    @NotBlank(message = "출연자를 입력하세요")
    private String artistNameKr;

    @NotBlank(message = "출연자 영문명을 입력하세요")
    private String artistNameEng;

    @NotBlank(message = "장소명을 입력하세요")
    private String placeName;

    @NotNull(message = "경도를 입력하세요")
    private Double longitude;

    @NotNull(message = "위도를 입력하세요")
    private Double latitude;

    @NotNull(message = "예매 오픈 일시를 입력하세요")
    private LocalDateTime releaseTime;

    @NotNull(message = " 공연 시작 시간을 입력하세요")
    private LocalDateTime startTime;

    @NotNull(message = "공연 종료 시간을 입력하세요")
    private LocalDateTime endTime;

    private ConcertCategory category;

    private ConcertStatus status;

    @NotNull(message = "좌석수를 입력하세요")
    @Min(0)
    private int totalPeople;

    @NotNull(message = "좌석 가격을 입력하세요")
    @Min(0)
    private int seatPrice;

    private List<MultipartFile> image;
}