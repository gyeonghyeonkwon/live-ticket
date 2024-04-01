package com.ll.ticket.domain.admin.controller;

import com.ll.ticket.domain.admin.dto.RegisterConcertDto;
import com.ll.ticket.domain.admin.service.AdminService;
import com.ll.ticket.domain.concert.entity.Concert;
import com.ll.ticket.domain.concert.entity.ConcertDate;
import com.ll.ticket.domain.concert.entity.ConcertPerformer;
import com.ll.ticket.domain.concert.entity.Image;
import com.ll.ticket.domain.concert.service.ConcertDateService;
import com.ll.ticket.domain.concert.service.ConcertPerformerService;
import com.ll.ticket.domain.concert.service.ConcertService;
import com.ll.ticket.domain.place.entity.Place;
import com.ll.ticket.domain.place.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final ConcertService concertService;
    private final ConcertDateService concertDateService;
    private final ConcertPerformerService concertPerformerService;
    private final PlaceService placeService;

    @Value("${kakaoMap.javascript.key}")
    private String mapApiKey;

    //관리자 페이지 메인화면
    @GetMapping(value = "/main")
    public String adminMain(){
        return "domain/admin/admin_main";
    }

    //콘서트 등록
    @GetMapping(value = "/registerConcert")
    public String registerConcert(RegisterConcertDto registerConcertDto, Model model) {
        model.addAttribute("apiKey" , mapApiKey);

        return "domain/admin/register_concert";
    }


    //콘서트 등록
    @PostMapping(value = "/registerConcert")
    public String registerConcert(@Valid RegisterConcertDto registerConcertDto, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "domain/admin/register_concert";
        }

        adminService.register(registerConcertDto);

        return "redirect:/admin/concertList";
    }

    //콘서트 목록
    @GetMapping(value = "/concertList")
    public String concertList(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
        Page<Concert> concertList = this.concertService.getConcertList(page);
        model.addAttribute("concertList", concertList);

        return "domain/admin/concert_list";
    }

    //관리자용 콘서트 글 상세 페이지
    @GetMapping(value = "/concertDetail/{id}")
    public String concertDetail(Model model, @PathVariable("id") Long id) {
        Concert concert = this.concertService.findById(id);
        Place place = this.concertService.findPlace(concert);
        ConcertPerformer concertPerformer = this.concertService.findConcertPerformer(concert);
        List<ConcertDate> concertDates = this.concertService.findConcertDates(concert);
        List<Image> images = this.concertService.findImages(concert);

        model.addAttribute("concert", concert);
        model.addAttribute("place", place);
        model.addAttribute("concertPerformer", concertPerformer);
        model.addAttribute("concertDates", concertDates);
        model.addAttribute("images", images);

        return "domain/admin/concert_detail";
    }
/*
    //콘서트 글 수정
    @GetMapping("/modifyConcert/{id}")
    public String modifyConcert(RegisterConcertDto registerConcertDto, @PathVariable("id") Long id, Model model){
        Concert concert = this.concertService.findById(id);
        Place place = this.concertService.findPlace(concert);
        ConcertPerformer concertPerformer = this.concertService.findConcertPerformer(concert);
        List<ConcertDate> concertDates = this.concertService.findConcertDates(concert);

        registerConcertDto.setName(concert.getName());
        registerConcertDto.setConcertNameKr(concert.getConcertNameKr());
        registerConcertDto.setConcertNameEng(concert.getConcertNameEng());
        registerConcertDto.setArtistNameKr(concertPerformer.getArtistNameKr());
        registerConcertDto.setArtistNameEng(concertPerformer.getArtistNameEng());
        registerConcertDto.setLatitude(place.getLatitude());
        registerConcertDto.setLongitude(place.getLongitude());
        registerConcertDto.setReleaseTime(concert.getReleaseTime());
        registerConcertDto.setStartTime(concertDates.get(0).getStartTime());
        registerConcertDto.setEndTime(concertDates.get(0).getEndTime());
        registerConcertDto.setCategory(concert.getCategory());
        registerConcertDto.setStatus(concert.getStatus());
        registerConcertDto.setTotalPeople(place.getTotalPeople());
        registerConcertDto.setSeatPrice(concert.getSeatPrice());

        model.addAttribute("registerConcertDto", registerConcertDto);
        return "domain/admin/register_concert";
    }

    //콘서트 글 수정
    @PostMapping("/modifyConcert/{id}")
    public String modifyConcert(@Valid RegisterConcertDto registerConcertDto, @PathVariable("id") Long id, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "domain/admin/register_concert";
        }
        Concert concert = this.concertService.findById(id);
        Place place = this.concertService.findPlace(concert);
        ConcertPerformer concertPerformer = this.concertService.findConcertPerformer(concert);
        List<ConcertDate> concertDates = this.concertService.findConcertDates(concert);

        this.adminService.modify(registerConcertDto, concert, place, concertPerformer, concertDates);
        return String.format("redirect:/admin/concertDetail/%s", id);
    }
*/

    //콘서트 글 삭제
    @GetMapping(value = "/deleteConcert/{id}")
    public String deleteConcert(@PathVariable("id") Long id){
        Concert concert = this.concertService.findById(id);
        this.concertService.delete(concert);

        return "redirect:/admin/concertList";
    }
}