package com.d203.backend.api.controller;

import com.d203.backend.api.request.RoomsDto;
import com.d203.backend.api.service.RoomsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RoomsController {
    private final RoomsService roomsService;

    @GetMapping("/api/v1/rooms")
    public List<RoomsDto.ListResponse> findAll() {
        return roomsService.findAll();
    }

    @PostMapping("/api/v1/rooms/create")
    public Long save(@RequestBody RoomsDto.Save requestDto) {
        return roomsService.save(requestDto);
    }
}
