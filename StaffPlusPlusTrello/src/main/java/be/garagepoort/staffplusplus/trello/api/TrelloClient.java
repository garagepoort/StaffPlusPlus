package be.garagepoort.staffplusplus.trello.api;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface TrelloClient {

    @RequestLine("GET /boards/{boardId}/lists")
    @Headers("Content-Type: application/json")
    List<TrelloListResponse> getListsOfBoard(@Param("boardId") String boardId);

    @RequestLine("POST /cards")
    @Headers("Content-Type: application/json")
    TrelloCardResponse createCard(TrelloCardRequest trelloCardRequest);

    @RequestLine("PUT /cards/{cardId}")
    @Headers("Content-Type: application/json")
    TrelloCardResponse updateCard(@Param("cardId") String cardId, TrelloCardRequest trelloCardRequest);
}
