package com.ll.ticket.domain.concert.service;

import com.ll.ticket.domain.concert.dto.ConcertResponse;
import com.ll.ticket.domain.concert.entity.*;
import com.ll.ticket.domain.concert.repository.ConcertDateRepository;
import com.ll.ticket.domain.concert.repository.ConcertRepository;
import com.ll.ticket.domain.concert.repository.ConcertSeatHistoryRepository;
import com.ll.ticket.domain.place.entity.Place;
import com.ll.ticket.domain.seat.entity.Seat;
import com.ll.ticket.domain.seat.repository.SeatRepository;
import com.ll.ticket.global.enums.ConcertCategory;
import com.ll.ticket.global.enums.ConcertStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final ConcertDateRepository concertDateRepository;
    private final ConcertSeatHistoryRepository concertSeatHistoryRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public void changeStatus(LocalDateTime todayDateTime) {
        LocalDateTime ableStartDateTime = todayDateTime.plusDays(2);

        concertRepository.findByReleaseTimeLessThanEqualAndStatus(todayDateTime, ConcertStatus.ABLE)
                .forEach(concert -> concert.setStatus(ConcertStatus.ENABLE));

        concertDateRepository.findByStartTimeLessThanEqual(ableStartDateTime)
                .forEach(concertDate -> concertDate.getConcert().setStatus(ConcertStatus.ABLE));
    }

    private void changeConcertStatusInRange(LocalDate startDate, LocalDate endDate, ConcertStatus status) {
        concertDateRepository.findAllByConcertDateInRange(startDate, endDate).forEach(concertDate -> {
            concertDate.getConcert().setStatus(status);
        });
    }

    public Page<Concert> getConcertList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 15, Sort.by(sorts));
        return this.concertRepository.findAll(pageable);
    }

    public Concert findById(Long id) {
        Optional<Concert> concert = concertRepository.findById(id);
        if (concert.isPresent()) {
            return concert.get();
        } else {
            throw new IllegalArgumentException("존재하지 않는 공연입니다.");
        }
    }


    public List<ConcertDate> findConcertDateByConcert(Concert concert) {
        return concertDateRepository.findAllByConcert(concert);
    }

    public Optional<ConcertDate> findConcertDateById(String concertDateId) {
        return concertDateRepository.findById(Long.parseLong(concertDateId));
    }

    public List<Long> findAllSeatNumberByConcertDate(ConcertDate concertDate) {
        List<ConcertSeatHistory> seatHistoryList = concertSeatHistoryRepository.findAllByConcertDate(concertDate);

        List<Long> reservedSeatIds = seatHistoryList.stream()
                .map(seatHistory -> seatHistory.getSeat().getSeatId())
                .collect(Collectors.toList());

        return reservedSeatIds;
    }

    public void delete(Concert concert) {
        this.concertRepository.delete(concert);
    }

    public List<Long> findAllSeatIdByPlace(Place place) {
        List<Seat> seatIdList = seatRepository.findAllByPlaceOrderBySeatIdAsc(place);

        List<Long> seatIds = seatIdList.stream()
                .map(seat -> seat.getSeatId())
                .collect(Collectors.toList());
        return seatIds;
    }

    public Place findPlace(Concert concert){
        return concert.getPlace();
    }

    public List<ConcertDate> findConcertDates(Concert concert){
        return concert.getConcertDates();
    }

    public ConcertPerformer findConcertPerformer(Concert concert){
        return concert.getConcertPerformer();
    }

    public List<Image> findImages(Concert concert){ return concert.getImages();}


    public List<ConcertResponse> findConcertsByCategory(String category) {
        return null;
    }

    public List<ConcertResponse> getAllConcert() {
        return null;
    }

    public List<ConcertResponse> getConcertByCategory(String category) {
        return null;
    }

    public List<Concert> categoryConcertList(String type) {

        System.out.println("type"+ type);
        switch (type) {
            case "solo":
                return concertRepository.findByCategoryConcerts(ConcertCategory.SOLO);
            case "idol":
                return concertRepository.findByCategoryConcerts(ConcertCategory.IDOL);
            case "meeting":
                return concertRepository.findByCategoryConcerts(ConcertCategory.MEETING);
            case "festival":
                return concertRepository.findByCategoryConcerts(ConcertCategory.FESTIVAL);

        }
        return new ArrayList<>();
    }
}
