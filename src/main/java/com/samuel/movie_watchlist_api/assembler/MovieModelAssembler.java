package com.samuel.movie_watchlist_api.assembler;

import com.samuel.movie_watchlist_api.controllers.MovieController;
import com.samuel.movie_watchlist_api.domain.dto.MovieDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MovieModelAssembler implements RepresentationModelAssembler<MovieDto, EntityModel<MovieDto>> {

    @Override
    public EntityModel<MovieDto> toModel(MovieDto dto) {
        return EntityModel.of(dto, linkTo(methodOn(MovieController.class).listMovies()).withRel("movies"));
    }

    @Override
    public CollectionModel<EntityModel<MovieDto>> toCollectionModel(Iterable<? extends MovieDto> dtos) {
        return RepresentationModelAssembler.super.toCollectionModel(dtos).add(linkTo(methodOn(MovieController.class)).withSelfRel());
    }
}
