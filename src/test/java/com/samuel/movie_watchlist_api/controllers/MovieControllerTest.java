package com.samuel.movie_watchlist_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.movie_watchlist_api.assembler.MovieModelAssembler;
import com.samuel.movie_watchlist_api.domain.MovieEntity;
import com.samuel.movie_watchlist_api.domain.dto.MovieDto;
import com.samuel.movie_watchlist_api.mappers.Mapper;
import com.samuel.movie_watchlist_api.repositories.MovieRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MovieRepository movieRepository;

    @MockitoBean
    private Mapper<MovieEntity, MovieDto> movieMapper;

    @MockitoBean
    private MovieModelAssembler movieModelAssembler;

    @Test
    void testThatGetMovieWithValidIDReturnsMovie() throws Exception {
        MovieEntity movie1 = new MovieEntity(1L, "Inception");
        MovieDto dto1 = new MovieDto(1L, "Inception");
        EntityModel<MovieDto> em1 = EntityModel.of(dto1,
                Link.of("/movies").withRel("movies"), Link.of("/movie/1").withSelfRel());

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie1));
        when(movieMapper.mapTo(movie1)).thenReturn(dto1);
        when(movieModelAssembler.toModel(dto1)).thenReturn(em1);

        mockMvc.perform(get("/movie/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$._links.movies.href").value(Matchers.endsWith("/movies")))
                .andExpect(jsonPath("$._links.self.href").value(Matchers.endsWith("/movie/1")));
    }

    @Test
    void testThatGetMovieWithInvalidIDReturnsNotFound() throws Exception {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/movie/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testThatGetMoviesReturnsMovies() throws Exception {
        MovieEntity movie1 = new MovieEntity(1L, "Inception");
        MovieEntity movie2 = new MovieEntity(2L, "Interstellar");
        MovieDto dto1 = new MovieDto(1L, "Inception");
        MovieDto dto2 = new MovieDto(2L, "Interstellar");
        EntityModel<MovieDto> em1 = EntityModel.of(dto1,
                Link.of("/movies").withRel("movies"), Link.of("/movie/1").withSelfRel());
        EntityModel<MovieDto> em2 = EntityModel.of(dto2,
                Link.of("/movies").withRel("movies"), Link.of("/movie/2").withSelfRel());

        CollectionModel<EntityModel<MovieDto>> collectionModel =
                CollectionModel.of(List.of(em1, em2),
                        Link.of("/movies").withSelfRel());

        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        when(movieMapper.mapTo(movie1)).thenReturn(dto1);
        when(movieMapper.mapTo(movie2)).thenReturn(dto2);
        when(movieModelAssembler.toCollectionModel(any())).thenReturn(collectionModel);

        mockMvc.perform(get("/movies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.movies[0].id").value("1"))
                .andExpect(jsonPath("$._embedded.movies[1].id").value("2"))
                .andExpect(jsonPath("$._embedded.movies[0].title").value("Inception"))
                .andExpect(jsonPath("$._embedded.movies[1].title").value("Interstellar"))
                .andExpect(jsonPath("$._embedded.movies[0]._links.movies.href", Matchers.endsWith("/movies")))
                .andExpect(jsonPath("$._embedded.movies[1]._links.movies.href", Matchers.endsWith("/movies")))
                .andExpect(jsonPath("$._embedded.movies[0]._links.self.href", Matchers.endsWith("/movie/1")))
                .andExpect(jsonPath("$._embedded.movies[1]._links.self.href", Matchers.endsWith("/movie/2")))
                .andExpect(jsonPath("$._links.self.href", Matchers.endsWith("/movies")));
    }

    @Test
    void testThatAddMovieInsertsandReturnsMovie() throws Exception {
        MovieDto movieDto = MovieDto.builder().title("Inception").build();
        MovieEntity movieEntity = MovieEntity.builder().title("Inception").build();
        MovieEntity movieEntityWithId = MovieEntity.builder().id(1L).title("Inception").build();
        MovieDto movieDtoWithId = MovieDto.builder().id(1L).title("Inception").build();
        EntityModel<MovieDto> entityModel = EntityModel.of(movieDtoWithId,
                Link.of("/movies").withRel("movies"), Link.of("/movie/1").withSelfRel());

        when(movieMapper.mapFrom(movieDto)).thenReturn(movieEntity);
        when(movieRepository.save(movieEntity)).thenReturn(movieEntityWithId);
        when(movieMapper.mapTo(movieEntityWithId)).thenReturn(movieDtoWithId);
        when(movieModelAssembler.toModel(movieDtoWithId)).thenReturn(entityModel);

        mockMvc.perform(post("/movies").contentType("application/json").content(objectMapper.writeValueAsString(movieDto)))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value("1"),
                        jsonPath("$.title").value("Inception"),
                        jsonPath("$._links.movies.href").value(Matchers.endsWith("/movies")),
                        jsonPath("$._links.self.href").value(Matchers.endsWith("/movie/1"))
                );
    }

    @Test
    void testThatAddMovieReturns400WhenMovieIDisProvided() throws Exception {
        MovieDto movieDto = MovieDto.builder().id(1L).title("Inception").build();
        mockMvc.perform(post("/movies").contentType("application/json").content(objectMapper.writeValueAsString(movieDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


}