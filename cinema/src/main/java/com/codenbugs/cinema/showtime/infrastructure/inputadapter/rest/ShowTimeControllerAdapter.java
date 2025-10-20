package com.codenbugs.cinema.showtime.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.common.infrastructure.annotation.WebAdapter;
import com.codenbugs.cinema.showtime.application.ports.input.CreatingShowTimeInputPort;
import com.codenbugs.cinema.showtime.application.ports.input.ListAllShowTimesByCinemaIdInputPort;
import com.codenbugs.cinema.showtime.application.usecase.createshowtime.CreateShowTimeCaseDto;
import com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto.CreateShowtimeRequestDto;
import com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto.ShowTimeResponseDto;
import com.codenbugs.cinema.showtime.infrastructure.inputadapter.mapper.ShowTimeRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/show-times")
@WebAdapter
@RequiredArgsConstructor
public class ShowTimeControllerAdapter {

    private final CreatingShowTimeInputPort creatingShowTimeInputPort;
    private final ListAllShowTimesByCinemaIdInputPort listAllShowTimesByCinemaIdInputPort;
    private final ShowTimeRestMapper mapper;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> createShowTime(@RequestBody @Valid CreateShowtimeRequestDto createShowtimeRequestDto){
        CreateShowTimeCaseDto caseDto = createShowtimeRequestDto.toCase();
        creatingShowTimeInputPort.creatShowTime(caseDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<List<ShowTimeResponseDto>> listAllShowTimesByCinemaId(@PathVariable UUID cinemaId){

        List<ShowTimeResponseDto> showTimes = listAllShowTimesByCinemaIdInputPort.listAllShowTimesByCinemaId(cinemaId)
                .stream()
                .map(mapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(showTimes);
    }
}
