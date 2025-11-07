package com.codenbugs.cinema.cinema.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.cinema.application.ports.input.*;
import com.codenbugs.cinema.cinema.application.usecase.createcinema.CreateCinemaDto;
import com.codenbugs.cinema.cinema.application.usecase.updatecinema.UpdateCinemaDto;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.cinema.infrastructure.inputadapter.dto.CinemaCreateRequestDto;
import com.codenbugs.cinema.cinema.infrastructure.inputadapter.dto.CinemaResponseDto;
import com.codenbugs.cinema.cinema.infrastructure.inputadapter.mapper.CinemaMapperRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CinemaControllerAdapterTest {
    @Mock
    private CreatingCinemaInputPort creatingCinemaInputPort;

    @Mock
    private FindingCinemaByIdAdminInputPort findingCinemaByIdAdminInputPort;

    @Mock
    private UpdatingCinemaByIdInputPort updatingCinemaByIdInputPort;

    @Mock
    private FindingCinemaByIdInputPort findingCinemaByIdInputPort;

    @Mock
    private FindAllCinemaInputPort findAllCinemaInputPort;

    @Mock
    private CinemaMapperRest cinemaMapperRest;

    @InjectMocks
    private CinemaControllerAdapter controller;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldCreateCinemaSuccessfully() throws Exception {
        UUID adminId = UUID.randomUUID();
        CinemaCreateRequestDto requestDto = new CinemaCreateRequestDto("CineTest", "img.png", "Address 123", adminId, BigDecimal.valueOf(100));
        CreateCinemaDto createCinemaDto = requestDto.toDomain();
        CinemaDomainEntity domainEntity = new CinemaDomainEntity(UUID.randomUUID(), "CINETEST", "img.png", "Address 123", adminId, BigDecimal.valueOf(100), Instant.now());
        CinemaResponseDto responseDto = CinemaResponseDto.builder()
                .id(domainEntity.getId())
                .name(domainEntity.getName())
                .imageUrl(domainEntity.getImageUrl())
                .address(domainEntity.getAddress())
                .adminUserId(domainEntity.getAdminUserId())
                .dailyCost(domainEntity.getDailyCost())
                .createdAt(domainEntity.getCreatedAt())
                .build();

        when(creatingCinemaInputPort.createCinema(createCinemaDto)).thenReturn(domainEntity);
        when(cinemaMapperRest.toResponseDto(domainEntity)).thenReturn(responseDto);

        mockMvc.perform(post("/v1/cinemas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(domainEntity.getId().toString()))
                .andExpect(jsonPath("$.name").value(domainEntity.getName()));

        verify(creatingCinemaInputPort, times(1)).createCinema(createCinemaDto);
        verify(cinemaMapperRest, times(1)).toResponseDto(domainEntity);
    }

    @Test
    void shouldFindCinemaByIdSuccessfully() throws Exception {
        UUID cinemaId = UUID.randomUUID();
        CinemaDomainEntity domainEntity = new CinemaDomainEntity(cinemaId, "CINETEST", "img.png", "Address 123", UUID.randomUUID(), BigDecimal.valueOf(100), Instant.now());
        CinemaResponseDto responseDto = CinemaResponseDto.builder()
                .id(domainEntity.getId())
                .name(domainEntity.getName())
                .imageUrl(domainEntity.getImageUrl())
                .address(domainEntity.getAddress())
                .adminUserId(domainEntity.getAdminUserId())
                .dailyCost(domainEntity.getDailyCost())
                .createdAt(domainEntity.getCreatedAt())
                .build();

        when(findingCinemaByIdInputPort.findCinemaById(cinemaId)).thenReturn(domainEntity);
        when(cinemaMapperRest.toResponseDto(domainEntity)).thenReturn(responseDto);

        mockMvc.perform(get("/v1/cinemas/{id}", cinemaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cinemaId.toString()))
                .andExpect(jsonPath("$.name").value(domainEntity.getName()));
    }

    @Test
    void shouldFindAllCinemasSuccessfully() throws Exception {
        CinemaDomainEntity cinema1 = new CinemaDomainEntity(UUID.randomUUID(), "CINE1", "img1.png", "Address1", UUID.randomUUID(), BigDecimal.valueOf(50), Instant.now());
        CinemaDomainEntity cinema2 = new CinemaDomainEntity(UUID.randomUUID(), "CINE2", "img2.png", "Address2", UUID.randomUUID(), BigDecimal.valueOf(60), Instant.now());

        CinemaResponseDto dto1 = CinemaResponseDto.builder()
                .id(cinema1.getId()).name(cinema1.getName())
                .imageUrl(cinema1.getImageUrl())
                .address(cinema1.getAddress())
                .adminUserId(cinema1.getAdminUserId())
                .dailyCost(cinema1.getDailyCost())
                .createdAt(cinema1.getCreatedAt())
                .build();

        CinemaResponseDto dto2 = CinemaResponseDto.builder()
                .id(cinema2.getId()).name(cinema2.getName())
                .imageUrl(cinema2.getImageUrl())
                .address(cinema2.getAddress())
                .adminUserId(cinema2.getAdminUserId())
                .dailyCost(cinema2.getDailyCost())
                .createdAt(cinema2.getCreatedAt())
                .build();

        when(findAllCinemaInputPort.findAll()).thenReturn(List.of(cinema1, cinema2));
        when(cinemaMapperRest.toResponseDto(cinema1)).thenReturn(dto1);
        when(cinemaMapperRest.toResponseDto(cinema2)).thenReturn(dto2);

        mockMvc.perform(get("/v1/cinemas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(cinema1.getId().toString()))
                .andExpect(jsonPath("$[1].id").value(cinema2.getId().toString()));
    }

    @Test
    void shouldUpdateCinemaSuccessfully() throws Exception {
        UUID cinemaId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();

        CinemaCreateRequestDto requestDto = new CinemaCreateRequestDto("CINE UPDATED", "img-updated.png", "Address Updated", adminId, BigDecimal.valueOf(150));
        UpdateCinemaDto updateDto = requestDto.toUpdateDomain();

        CinemaDomainEntity updatedDomain = new CinemaDomainEntity(cinemaId, "CINE UPDATED", "img-updated.png", "Address Updated", adminId, BigDecimal.valueOf(150), Instant.now());
        CinemaResponseDto responseDto = CinemaResponseDto.builder()
                .id(updatedDomain.getId())
                .name(updatedDomain.getName())
                .imageUrl(updatedDomain.getImageUrl())
                .address(updatedDomain.getAddress())
                .adminUserId(updatedDomain.getAdminUserId())
                .dailyCost(updatedDomain.getDailyCost())
                .createdAt(updatedDomain.getCreatedAt())
                .build();

        when(updatingCinemaByIdInputPort.updatingCinemaById(cinemaId, updateDto)).thenReturn(updatedDomain);
        when(cinemaMapperRest.toResponseDto(updatedDomain)).thenReturn(responseDto);

        mockMvc.perform(put("/v1/cinemas/{idCinema}", cinemaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(cinemaId.toString()))
                .andExpect(jsonPath("$.name").value("CINE UPDATED"));
    }

    @Test
    void shouldFindCinemaByAdminIdSuccessfully() throws Exception {
        UUID adminId = UUID.randomUUID();
        CinemaDomainEntity domainEntity = new CinemaDomainEntity(UUID.randomUUID(), "CINETEST", "img.png", "Address 123", adminId, BigDecimal.valueOf(100), Instant.now());
        CinemaResponseDto responseDto = CinemaResponseDto.builder()
                .id(domainEntity.getId())
                .name(domainEntity.getName())
                .imageUrl(domainEntity.getImageUrl())
                .address(domainEntity.getAddress())
                .adminUserId(domainEntity.getAdminUserId())
                .dailyCost(domainEntity.getDailyCost())
                .createdAt(domainEntity.getCreatedAt())
                .build();

        when(findingCinemaByIdAdminInputPort.findCinemaByIdAdmin(adminId)).thenReturn(domainEntity);
        when(cinemaMapperRest.toResponseDto(domainEntity)).thenReturn(responseDto);

        mockMvc.perform(get("/v1/cinemas/admin/{idAdmin}", adminId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adminUserId").value(adminId.toString()));
    }
}
