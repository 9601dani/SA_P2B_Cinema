package com.codenbugs.cinema.cinema.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.cinema.application.ports.input.CreatingCinemaInputPort;
import com.codenbugs.cinema.cinema.application.ports.input.FindingCinemaByIdAdminInputPort;
import com.codenbugs.cinema.cinema.application.ports.input.FindingCinemaByIdInputPort;
import com.codenbugs.cinema.cinema.application.ports.input.UpdatingCinemaByIdInputPort;
import com.codenbugs.cinema.cinema.application.usecase.createcinema.CreateCinemaDto;
import com.codenbugs.cinema.cinema.application.usecase.updatecinema.UpdateCinemaDto;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.cinema.infrastructure.inputadapter.dto.CinemaCreateRequestDto;
import com.codenbugs.cinema.cinema.infrastructure.inputadapter.dto.CinemaResponseDto;
import com.codenbugs.cinema.cinema.infrastructure.inputadapter.mapper.CinemaMapperRest;
import com.codenbugs.cinema.common.infrastructure.annotation.WebAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/cinemas")
@WebAdapter
@RequiredArgsConstructor
public class CinemaControllerAdapter {

    private final CreatingCinemaInputPort createCinemaInputPort;
    private final CinemaMapperRest cinemaMapperRest;
    private final FindingCinemaByIdAdminInputPort findingCinemaByIdAdminInputPort;
    private final UpdatingCinemaByIdInputPort updatingCinemaByIdInputPort;
    private final FindingCinemaByIdInputPort findingCinemaByIdInputPort;

    @PostMapping
    @Transactional
    public ResponseEntity<CinemaResponseDto> createCinema(@RequestBody @Valid CinemaCreateRequestDto cinemaCreateRequestDto){

        CreateCinemaDto cinemaDto = cinemaCreateRequestDto.toDomain();
        CinemaDomainEntity cinemaDomain = createCinemaInputPort.createCinema(cinemaDto);

        CinemaResponseDto cinemaResponseDto = cinemaMapperRest.toResponseDto(cinemaDomain);
        return ResponseEntity.status(HttpStatus.CREATED).body(cinemaResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaResponseDto> findCinemaById(@PathVariable UUID id) {
        CinemaResponseDto cinemaResponseDto = cinemaMapperRest.toResponseDto(findingCinemaByIdInputPort.findCinemaById(id));
        return ResponseEntity.ok(cinemaResponseDto);
    }

    @GetMapping("/admin/{idAdmin}")
    public ResponseEntity<CinemaResponseDto> findCinemaByIdAdmin(@PathVariable UUID idAdmin){
        CinemaDomainEntity cinemaDomain = findingCinemaByIdAdminInputPort.findCinemaByIdAdmin(idAdmin);
        return ResponseEntity.ok(cinemaMapperRest.toResponseDto(cinemaDomain));
    }


    @PutMapping("{idCinema}")
    public ResponseEntity<CinemaResponseDto> updateCinema(@PathVariable UUID idCinema, @RequestBody @Valid CinemaCreateRequestDto cinemaCreateRequestDto){

        UpdateCinemaDto updateCinemaDto = cinemaCreateRequestDto.toUpdateDomain();
        CinemaDomainEntity cinemaDomain = updatingCinemaByIdInputPort.updatingCinemaById(idCinema, updateCinemaDto);

        CinemaResponseDto cinemaResponseDto = cinemaMapperRest.toResponseDto(cinemaDomain);
        return ResponseEntity.status(HttpStatus.CREATED).body(cinemaResponseDto);
    }


}
