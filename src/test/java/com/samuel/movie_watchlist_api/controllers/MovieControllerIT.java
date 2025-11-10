package com.samuel.movie_watchlist_api.controllers;

import com.samuel.movie_watchlist_api.domain.MovieEntity;
import com.samuel.movie_watchlist_api.repositories.MovieRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MovieControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    private MovieEntity movie1;
    private MovieEntity movie2;

    @BeforeEach
    void setUp() {
        movie1 = movieRepository.save(new MovieEntity(null, "Inception"));
        movie2 = movieRepository.save(new MovieEntity(null, "Interstellar"));
    }

    @Test
    void testThatListMoviesReturnsMovies() throws Exception {
        mockMvc.perform(get("/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.movies[0].id").value(movie1.getId()))
                .andExpect(jsonPath("$._embedded.movies[1].id").value(movie2.getId()))
                .andExpect(jsonPath("$._embedded.movies[0].title").value("Inception"))
                .andExpect(jsonPath("$._embedded.movies[1].title").value("Interstellar"))
                .andExpect(jsonPath("$._embedded.movies[0]._links.movies.href", Matchers.endsWith("/movies")))
                .andExpect(jsonPath("$._embedded.movies[1]._links.movies.href", Matchers.endsWith("/movies")))
                .andExpect(jsonPath("$._links.self.href", Matchers.endsWith("/movies")));
    }
}
