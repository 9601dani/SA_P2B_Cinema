package com.codenbugs.cinema.showtime.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.common.infrastructure.annotation.WebAdapter;
import com.codenbugs.cinema.showtime.application.ports.input.*;
import com.codenbugs.cinema.showtime.application.usecase.createshowtime.CreateShowTimeCaseDto;
import com.codenbugs.cinema.showtime.application.usecase.reporting.ReportingRangeDto;
import com.codenbugs.cinema.showtime.application.usecase.updateactive.UpdateActiveCase;
import com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto.*;
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
    private final UpdatingShowTimeActiveByIdInputPort updatingShowTimeActiveByIdInputPort;
    private final ReportingShowTimesPerRoomByCinemaIdInputPort reportingShowTimesPerRoomByCinemaIdInputPort;
    private final ReportingShowTimesPerRoomByCinemaIdRangeDateInputPort reportingShowTimesPerRoomByCinemaIdRangeDateInputPort;

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

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateShowtimeStatus(@PathVariable UUID id, @RequestBody @Valid UpdateStatusRequestDto updateStatusRequestDto){
        UpdateActiveCase caseDto = new UpdateActiveCase(updateStatusRequestDto.active(), id);
        updatingShowTimeActiveByIdInputPort.updateActive(caseDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // reports
    @PostMapping("/reports/range/cinema/rooms" )
    public ResponseEntity<List<ReportShowTimesRoomsDto>> reportingShowTimesPerRoomByCinemaIdRangeDate(@RequestBody @Valid ReportingRangeRequestDto requestDto){
        ReportingRangeDto range = requestDto.toCase();
        var report = reportingShowTimesPerRoomByCinemaIdRangeDateInputPort.reportShowTimesPerRoomByCinemaIdRangeDate(range)
                .stream()
                .map(mapper::toReportResponseDto)
                .toList();

        return ResponseEntity.ok(report);
    }

    @GetMapping("/reports/cinema/{cinemaId}/rooms" )
    public ResponseEntity<List<ReportShowTimesRoomsDto>> reportingShowTimesPerRoomByCinemaId(@PathVariable UUID cinemaId){
        var report = reportingShowTimesPerRoomByCinemaIdInputPort.reportShowTimesPerRoomByCinemaId(cinemaId)
                .stream()
                .map(mapper::toReportResponseDto)
                .toList();

        return ResponseEntity.ok(report);
    }

}
