package com.samuel.movie_watchlist_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.movie_watchlist_api.assembler.MovieModelAssembler;
import com.samuel.movie_watchlist_api.domain.MovieEntity;
import com.samuel.movie_watchlist_api.domain.dto.MovieDto;
import com.samuel.movie_watchlist_api.mappers.Mapper;
import com.samuel.movie_watchlist_api.repositories.MovieRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieRepository movieRepository;

    @MockitoBean
    private Mapper<MovieEntity, MovieDto> movieMapper;

    @MockitoBean
    private MovieModelAssembler movieModelAssembler;

    @Test
    void testThatListMoviesReturnsMovies() throws Exception {
        MovieEntity movie1 = new MovieEntity(1L, "Inception");
        MovieEntity movie2 = new MovieEntity(2L, "Interstellar");
        MovieDto dto1 = new MovieDto(1L, "Inception");
        MovieDto dto2 = new MovieDto(2L, "Interstellar");
        EntityModel<MovieDto> em1 = EntityModel.of(dto1,
                Link.of("/movies").withRel("movies"));
        EntityModel<MovieDto> em2 = EntityModel.of(dto2,
                Link.of("/movies").withRel("movies"));

        CollectionModel<EntityModel<MovieDto>> collectionModel =
                CollectionModel.of(List.of(em1, em2),
                        Link.of("/movies").withSelfRel());

        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        when(movieMapper.mapTo(movie1)).thenReturn(dto1);
        when(movieMapper.mapTo(movie2)).thenReturn(dto2);
        when(movieModelAssembler.toCollectionModel(any())).thenReturn(collectionModel);

        mockMvc.perform(get("/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.movies[0].id").value("1"))
                .andExpect(jsonPath("$._embedded.movies[1].id").value("2"))
                .andExpect(jsonPath("$._embedded.movies[0].title").value("Inception"))
                .andExpect(jsonPath("$._embedded.movies[1].title").value("Interstellar"))
                .andExpect(jsonPath("$._embedded.movies[0]._links.movies.href", Matchers.endsWith("/movies")))
                .andExpect(jsonPath("$._embedded.movies[1]._links.movies.href", Matchers.endsWith("/movies")))
                .andExpect(jsonPath("$._links.self.href", Matchers.endsWith("/movies")));
    }
}