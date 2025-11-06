package com.samuel.movie_watchlist_api.mappers;

public interface Mapper<Entity, Dto> {

    Dto mapTo(Entity entity);

    Entity mapFrom(Dto dto);

}
