package com.samuel.movie_watchlist_api;

import com.samuel.movie_watchlist_api.controllers.MovieController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MovieWatchlistApiApplicationTests {

    @Autowired
    private MovieController movieController;

	@Test
	void contextLoads() {
        assertThat(movieController).isNotNull();
	}

}
