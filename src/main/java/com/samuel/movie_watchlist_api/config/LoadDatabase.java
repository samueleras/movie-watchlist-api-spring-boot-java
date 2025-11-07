package com.samuel.movie_watchlist_api.config;

import com.samuel.movie_watchlist_api.domain.MovieEntity;
import com.samuel.movie_watchlist_api.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(MovieRepository movieRepository) {

        return args -> {
            log.info("Clearing old data ");
            movieRepository.deleteAll();
            ArrayList<MovieEntity> movieEntityList = new ArrayList<>();
            for (int i = 1; i <= 30; i++) {
                movieEntityList.add(MovieEntity.builder().title("Harry Potter " + i ).build());
            }
            log.info("Preloading " + movieRepository.saveAll(movieEntityList));
        };

    }

}
