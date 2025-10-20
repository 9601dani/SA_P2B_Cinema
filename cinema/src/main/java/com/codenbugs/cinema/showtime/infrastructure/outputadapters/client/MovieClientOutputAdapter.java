package com.codenbugs.cinema.showtime.infrastructure.outputadapters.client;

import com.codenbugs.cinema.common.infrastructure.exception.ExternalServiceException;
import com.codenbugs.cinema.showtime.application.ports.output.FindMovieByIdOutputPort;
import com.codenbugs.cinema.showtime.domain.model.MovieDomainEntity;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.client.dto.MovieResponseDto;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.client.mapper.MovieRestMapper;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.client.rest.MovieRestClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieClientOutputAdapter implements FindMovieByIdOutputPort {

    private final MovieRestClient movieRestClient;
    private final MovieRestMapper movieRestMapper;

    @Override
    public MovieDomainEntity findById(UUID id) {
        try {
            MovieResponseDto movieResponseDto = movieRestClient.findMovieById(id);
            return movieRestMapper.toDomain(movieResponseDto);
        }catch (FeignException e) {
            e.getStackTrace();
            throw new ExternalServiceException("No se pudo obtener la pelicula para crear la funcion.");
        }
    }
}
