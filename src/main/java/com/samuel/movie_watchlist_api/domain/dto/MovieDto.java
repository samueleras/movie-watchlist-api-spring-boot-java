package com.samuel.movie_watchlist_api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Relation(itemRelation = "movie", collectionRelation = "movies")
public class MovieDto {

    private Long id;

    private String title;

}