package com.samuel.movie_watchlist_api.mappers.impl;

import com.samuel.movie_watchlist_api.domain.MovieEntity;
import com.samuel.movie_watchlist_api.domain.dto.MovieDto;
import com.samuel.movie_watchlist_api.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MovieMapperImpl implements Mapper<MovieEntity, MovieDto> {

    private final ModelMapper modelMapper;

    public MovieMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public MovieDto mapTo(MovieEntity movieEntity) {
        return modelMapper.map(movieEntity, MovieDto.class);
    }

    @Override
    public MovieEntity mapFrom(MovieDto movieDto) {
        return modelMapper.map(movieDto, MovieEntity.class);
    }
}
