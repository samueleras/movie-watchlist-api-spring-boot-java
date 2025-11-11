package com.samuel.movie_watchlist_api.Exceptions;

public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(Long id){
        super("Could not find movie " + id);
    }

}