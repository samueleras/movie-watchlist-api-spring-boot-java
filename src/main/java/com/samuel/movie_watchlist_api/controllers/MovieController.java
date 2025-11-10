package com.samuel.movie_watchlist_api.controllers;

import com.samuel.movie_watchlist_api.assembler.MovieModelAssembler;
import com.samuel.movie_watchlist_api.domain.MovieEntity;
import com.samuel.movie_watchlist_api.domain.dto.MovieDto;
import com.samuel.movie_watchlist_api.mappers.Mapper;
import com.samuel.movie_watchlist_api.repositories.MovieRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    private final MovieRepository movieRepository;
    private final Mapper<MovieEntity, MovieDto>  movieMapper;
    private final MovieModelAssembler movieModelAssembler;

    public MovieController(MovieRepository movieRepository, Mapper<MovieEntity, MovieDto>  movieMapper, MovieModelAssembler movieModelAssembler) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.movieModelAssembler = movieModelAssembler;
    }

    @GetMapping("/movies")
    public ResponseEntity<CollectionModel<EntityModel<MovieDto>>> listMovies() {
        List<MovieEntity> movieEntities = movieRepository.findAll();
        List<MovieDto> movieDtos = movieEntities.stream().map(movieMapper::mapTo).toList();
        CollectionModel<EntityModel<MovieDto>> collectionModel = movieModelAssembler.toCollectionModel(movieDtos);
        return new ResponseEntity<CollectionModel<EntityModel<MovieDto>>>(collectionModel, HttpStatus.OK);
    }

}
