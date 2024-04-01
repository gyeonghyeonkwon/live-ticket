package com.ll.ticket.domain.concert.service;

import com.ll.ticket.domain.concert.datailPage.detailPagedto.ConcertDateDTO;
import com.ll.ticket.domain.concert.entity.Concert;
import com.ll.ticket.domain.concert.repository.ConcertTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConcertDateCalService {

    private final ConcertTimeRepository concertTimeRepository;

    public List<ConcertDateDTO> findConcertDateByConcert(Concert concert) {
        return concertTimeRepository.findByConcert(concert).stream()
                .map(ConcertDateDTO::new) // ConcertDate를 ConcertDate DTO로 변환
                .collect(Collectors.toList());
    }

    /**
     *
     * 관람시간 계산
     *
     * concertDate : endtime - startime
     * */
    public Duration calculateTotalViewingTime(List<ConcertDateDTO> concertDates) {
        return concertDates.stream()
                .map(concertDateDTO -> Duration.between(concertDateDTO.getStartTime(), concertDateDTO.getEndTime()))
                .distinct()// 시간 중복 되지않음
                .reduce(Duration::plus)
                .orElse(Duration.ZERO);
    }

}

