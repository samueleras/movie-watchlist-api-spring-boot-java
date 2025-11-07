package com.samuel.movie_watchlist_api.repositories;

import com.samuel.movie_watchlist_api.domain.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

}
