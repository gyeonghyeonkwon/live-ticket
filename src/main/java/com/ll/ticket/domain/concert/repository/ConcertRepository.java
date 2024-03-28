package com.ll.ticket.domain.concert.repository;

import com.ll.ticket.domain.concert.entity.Concert;
import com.ll.ticket.global.enums.ConcertCategory;
import com.ll.ticket.global.enums.ConcertStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
public interface ConcertRepository extends JpaRepository<Concert, Long> {
    Page<Concert> findAll(Pageable pageable);

    @Query("SELECT distinct c FROM Concert c join fetch c.place p left join fetch c.concertPerformer cp left join fetch c.images " +
            "WHERE LOWER(c.concertNameKr) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(cp.artistNameEng) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(cp.artistNameKr) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(c.concertNameEng) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(p.placeName) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(c.category) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "AND c.status = 'ENABLE'")
    List<Concert> findByConcertKeyword(@Param("keyword") String keyword);

    @Query("SELECT distinct c FROM Concert c " +
            "left join fetch c.place p " +
            "left join fetch c.concertPerformer cp " +
            "left join fetch c.images i " +
            "join c.concertDates cd " +
            "WHERE c.category = :type " +
            "AND c.status = 'ENABLE'")
    List<Concert> findByCategoryConcerts(@Param("type") ConcertCategory type);

    List<Concert> findByReleaseTimeLessThanEqualAndStatus(LocalDateTime todayDateTime, ConcertStatus concertStatus);
}