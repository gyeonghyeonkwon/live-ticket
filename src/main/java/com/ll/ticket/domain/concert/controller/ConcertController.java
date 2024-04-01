package com.ll.ticket.domain.concert.controller;

import com.ll.ticket.domain.concert.entity.Concert;
import com.ll.ticket.domain.concert.entity.ConcertDate;
import com.ll.ticket.domain.concert.service.ConcertService;
import com.ll.ticket.global.security.config.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ConcertController {
    private final ConcertService concertService;

    @GetMapping("/concert/{id}")
    public String getConcert(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal SecurityUser user) {
        try {
            Concert concert = concertService.findById(id);
            List<ConcertDate> concertDate = concertService.findConcertDateByConcert(concert);

            if (concertDate.isEmpty()) {
                throw new IllegalArgumentException("공연 날짜가 존재하지 않습니다.");
            }

            model.addAttribute("concert", concert);
            model.addAttribute("concertDate", concertDate);
            model.addAttribute("member", user);

            return "domain/concert/concert";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "main";
        }


    }
    @GetMapping("/concert/category/{type}")
    public String getConcertByCategory(@PathVariable("type") String type, Model model, @AuthenticationPrincipal SecurityUser user) {
        List<Concert> concerts = concertService.categoryConcertList(type);
        for (Concert concert : concerts) {
            String image = concert.getImages().get(0).toString();
            System.out.println(image);
        }
        System.out.println("concerts.size() = " + concerts.size());
        model.addAttribute("concerts", concerts);
        model.addAttribute("member", user);
        return "domain/concert/category";
    }

}
