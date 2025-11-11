package com.samuel.movie_watchlist_api.Exceptions;


public class InvalidInputException extends RuntimeException{

    public InvalidInputException(String msg){
        super(msg);
    }

}
