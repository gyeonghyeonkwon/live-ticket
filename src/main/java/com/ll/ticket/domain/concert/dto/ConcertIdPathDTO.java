package com.ll.ticket.domain.concert.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConcertIdPathDTO {
    private Long concertId;
    private String path;
}
