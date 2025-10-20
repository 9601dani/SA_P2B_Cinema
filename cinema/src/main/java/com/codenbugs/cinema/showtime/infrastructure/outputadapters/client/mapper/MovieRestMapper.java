package com.codenbugs.cinema.showtime.infrastructure.outputadapters.client.mapper;

import com.codenbugs.cinema.showtime.domain.model.CategoryDomainEntity;
import com.codenbugs.cinema.showtime.domain.model.MovieDomainEntity;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.client.dto.CategoryResponseDto;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.client.dto.MovieResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieRestMapper {

    private CategoryDomainEntity toDomainCategory(CategoryResponseDto categoryResponseDto) {
        if (categoryResponseDto == null) {
            return null;
        }

        return new CategoryDomainEntity(categoryResponseDto.id(), categoryResponseDto.name());
    }

    public MovieDomainEntity toDomain(MovieResponseDto movieResponseDto) {
        if (movieResponseDto == null) {
            return null;
        }

        List<CategoryDomainEntity> categories = movieResponseDto.categories() != null
                ? movieResponseDto.categories().stream()
                .map(this::toDomainCategory)
                .toList()
                : List.of();

        return new MovieDomainEntity(movieResponseDto.id(), movieResponseDto.title(),
                movieResponseDto.durationMinutes(), movieResponseDto.posterUrl(),
                categories, movieResponseDto.active());

    }


}
