package com.ll.ticket.domain.concert.scheduler;

import com.ll.ticket.domain.concert.service.ConcertService;
import com.ll.ticket.domain.concert.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class ConcertScheduler {
    private final ConcertService concertService;
    private final QueueService queueService;
    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(cron = "0 0 * * * *") // 운영-매일 0시
//    @Scheduled(cron = "0 * * * * *") // 테스트용-매분 0초
    public void runConcertChangeStatus() {
         LocalDateTime todayDateTime = LocalDateTime.now();

         concertService.changeStatus(todayDateTime);
    }

    @Scheduled(fixedDelay = 500)
    private void queueEventScheduler() {
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            queueService.processQueue(key);
            queueService.listQueue(key);
        }
    }
}
