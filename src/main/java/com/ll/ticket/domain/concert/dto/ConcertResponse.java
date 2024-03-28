package com.ll.ticket.domain.concert.dto;

import com.ll.ticket.domain.concert.entity.Concert;
import com.ll.ticket.domain.concert.entity.ConcertDate;
import com.ll.ticket.domain.place.entity.Place;
import com.ll.ticket.global.enums.ConcertCategory;
import com.ll.ticket.global.enums.ConcertStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertResponse {
    // 콘서트 한글명 ,영문명
    private long id;
    private String name;
    private String concertNameKr;
    private String concertNameEng;
    private Place place;
    private LocalDateTime releaseTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int runningTime;
    private ConcertCategory category;
    private ConcertStatus status;
    private int seatPrice;
    private List<String> fullPath;
    private String image;
    private List<ConcertDate> concertDates;


    public static ConcertResponse of(Concert concert){
        return ConcertResponse.builder()
                .id(concert.getConcertId())
                .name(concert.getName())
                .concertNameKr(concert.getConcertNameKr())
                .concertNameEng(concert.getConcertNameEng())
                .place(concert.getPlace())
                .releaseTime(concert.getReleaseTime())
//                .startTime(concertdate.getStartTime())
//                .endTime(concert.getEndTime())
                .runningTime(concert.getRunningTime())
                .category(concert.getCategory())
                .status(concert.getStatus())
                .seatPrice(concert.getSeatPrice())
//                .concertDates(concert.getConcertDates())
                .image(concert.getImages().get(0).getPath())
                .build();
    }



    public static ConcertResponse of(Concert concert, List<String> fullPath){
        return ConcertResponse.builder()
                .id(concert.getConcertId())
                .name(concert.getName())
                .concertNameKr(concert.getConcertNameKr())
                .concertNameEng(concert.getConcertNameEng())
                .place(concert.getPlace())
                .releaseTime(concert.getReleaseTime())
//                .startTime(concert.getStartTime())
//                .endTime(concert.getEndTime())
                .runningTime(concert.getRunningTime())
                .category(concert.getCategory())
                .status(concert.getStatus())
                .seatPrice(concert.getSeatPrice())
                .build();
    }
}
