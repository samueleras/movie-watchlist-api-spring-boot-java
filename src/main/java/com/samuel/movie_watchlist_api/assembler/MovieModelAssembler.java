package com.samuel.movie_watchlist_api.assembler;

import com.samuel.movie_watchlist_api.controllers.MovieController;
import com.samuel.movie_watchlist_api.domain.dto.MovieDto;
import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MovieModelAssembler implements RepresentationModelAssembler<MovieDto, EntityModel<MovieDto>> {

    @Override
    public @NonNull EntityModel<MovieDto> toModel(@NonNull MovieDto dto) {
        return EntityModel.of(dto, linkTo(methodOn(MovieController.class).getMovie(dto.getId())).withSelfRel(), linkTo(methodOn(MovieController.class).getMovies()).withRel("movies"));
    }

    @Override
    public @NonNull CollectionModel<EntityModel<MovieDto>> toCollectionModel(@NonNull Iterable<? extends MovieDto> dtos) {
        return RepresentationModelAssembler.super.toCollectionModel(dtos).add(linkTo(methodOn(MovieController.class)).withSelfRel());
    }
}
