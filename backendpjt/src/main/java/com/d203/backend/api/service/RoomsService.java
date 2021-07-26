package com.d203.backend.api.service;

import com.d203.backend.api.request.RoomsDto;
import com.d203.backend.db.repository.RoomsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoomsService {
    private final RoomsRepository roomsRepository;

    @Transactional(readOnly = true)
    public List<RoomsDto.ListResponse> findAll() {
        return roomsRepository.findAll().stream()
                .map(RoomsDto.ListResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(RoomsDto.Save requestDto) {
        return roomsRepository.save(requestDto.toEntity()).getId();
    }
}
