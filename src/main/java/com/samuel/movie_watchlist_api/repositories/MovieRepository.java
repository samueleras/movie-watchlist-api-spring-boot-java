package com.samuel.movie_watchlist_api.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.movie_watchlist_api.domain.MovieEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class MovieRepository {

    ObjectMapper objectMapper;

    public MovieRepository(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public List<MovieEntity> getAll() throws IOException {
        List<MovieEntity> movies = objectMapper.readValue(new File("src/main/resources/movie.json"), new TypeReference<List<MovieEntity>>(){});
        return movies;
    }

}
