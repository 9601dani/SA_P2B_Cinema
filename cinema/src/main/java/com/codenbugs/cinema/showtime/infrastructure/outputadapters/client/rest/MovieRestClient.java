package com.codenbugs.cinema.showtime.infrastructure.outputadapters.client.rest;

import com.codenbugs.cinema.showtime.infrastructure.outputadapters.client.dto.MovieResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "movie", url = "${client.services.movie}/api/movie")
public interface MovieRestClient {

    @GetMapping("/v1/movies/{id}")
    MovieResponseDto findMovieById(@PathVariable UUID id);
}
