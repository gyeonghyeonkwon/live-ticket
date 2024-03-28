package com.ll.ticket.domain.concert.service;

import com.ll.ticket.domain.concert.dto.QueueRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    public void addQueue(QueueRequestDto queueRequestDto) {
        String memberId = queueRequestDto.getMemberId();
        String concertId = queueRequestDto.getConcertId();
        long now = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(concertId, memberId, now);
    }

    public void processQueue(String eventName) {
        Set<String> queue = redisTemplate.opsForZSet().range(eventName, 0, 4);

        for (String memberId : queue) {
            redisTemplate.opsForZSet().remove(eventName, memberId);
            rabbitTemplate.convertAndSend("amq.direct", "processQueue." + memberId, "대기열 통과");
        }
    }

    public void listQueue(String eventName) {
        Set<String> queue = redisTemplate.opsForZSet().range(eventName, 0, -1);

        for (String memberId : queue) {
            Long rank = redisTemplate.opsForZSet().rank(eventName, memberId);

            rabbitTemplate.convertAndSend("amq.direct", "listQueue." + memberId, rank);
        }
    }
}