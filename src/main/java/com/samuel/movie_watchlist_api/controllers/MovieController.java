package com.samuel.movie_watchlist_api.controllers;

import com.samuel.movie_watchlist_api.Exceptions.InvalidInputException;
import com.samuel.movie_watchlist_api.Exceptions.MovieNotFoundException;
import com.samuel.movie_watchlist_api.assembler.MovieModelAssembler;
import com.samuel.movie_watchlist_api.domain.MovieEntity;
import com.samuel.movie_watchlist_api.domain.dto.MovieDto;
import com.samuel.movie_watchlist_api.mappers.Mapper;
import com.samuel.movie_watchlist_api.repositories.MovieRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/movie/{id}")
    public ResponseEntity<EntityModel<MovieDto>> getMovie(@PathVariable Long id) {
        MovieEntity movieEntity = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
        MovieDto movieDto = movieMapper.mapTo(movieEntity);
        EntityModel<MovieDto> movieModel = movieModelAssembler.toModel(movieDto);
        return new ResponseEntity<>(movieModel, HttpStatus.OK);
    }

    @GetMapping("/movies")
    public ResponseEntity<CollectionModel<EntityModel<MovieDto>>> getMovies() {
        List<MovieEntity> movieEntities = movieRepository.findAll();
        List<MovieDto> movieDtos = movieEntities.stream().map(movieMapper::mapTo).toList();
        CollectionModel<EntityModel<MovieDto>> collectionModel = movieModelAssembler.toCollectionModel(movieDtos);
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PostMapping("/movies")
    public ResponseEntity<EntityModel<MovieDto>> addMovie(@RequestBody MovieDto movieDto) {
        if (movieDto.getId() != null) {
            throw new InvalidInputException("ID must not be provided for new movies");
        }
        MovieEntity movieEntity = movieMapper.mapFrom(movieDto);
        movieEntity = movieRepository.save(movieEntity);
        movieDto = movieMapper.mapTo(movieEntity);
        EntityModel<MovieDto> movieModel = movieModelAssembler.toModel(movieDto);
        return new ResponseEntity<>(movieModel, HttpStatus.CREATED);
    }

}
