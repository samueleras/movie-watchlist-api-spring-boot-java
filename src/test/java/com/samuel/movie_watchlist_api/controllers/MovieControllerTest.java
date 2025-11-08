package com.samuel.movie_watchlist_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.movie_watchlist_api.domain.MovieEntity;
import com.samuel.movie_watchlist_api.domain.dto.MovieDto;
import com.samuel.movie_watchlist_api.mappers.Mapper;
import com.samuel.movie_watchlist_api.repositories.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MovieRepository movieRepository;

    @MockitoBean
    private Mapper<MovieEntity, MovieDto> movieMapper;

    @Test
    void testThatListMoviesReturnsMovies() throws Exception {
        MovieEntity movie1 = new MovieEntity(1L, "Inception");
        MovieEntity movie2 = new MovieEntity(2L, "Interstellar");
        MovieDto dto1 = new MovieDto(1L, "Inception");
        MovieDto dto2 = new MovieDto(2L, "Interstellar");

        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        when(movieMapper.mapTo(movie1)).thenReturn(dto1);
        when(movieMapper.mapTo(movie2)).thenReturn(dto2);

        mockMvc.perform(get("/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(dto1, dto2))));
    }
}