package com.samuel.movie_watchlist_api.controllers;

import com.samuel.movie_watchlist_api.domain.MovieEntity;
import com.samuel.movie_watchlist_api.domain.dto.MovieDto;
import com.samuel.movie_watchlist_api.mappers.Mapper;
import com.samuel.movie_watchlist_api.repositories.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    private final MovieRepository movieRepository;
    private final Mapper<MovieEntity, MovieDto>  movieMapper;

    public MovieController(MovieRepository movieRepository, Mapper<MovieEntity, MovieDto>  movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> listMovies() {
        try {
            List<MovieEntity> movies = movieRepository.findAll();
            List<MovieDto> movieDtos = movies.stream().map(movieMapper::mapTo).toList();
            return new ResponseEntity<>(movieDtos, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
