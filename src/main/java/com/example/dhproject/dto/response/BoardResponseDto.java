package com.example.dhproject.dto.response;

import com.example.dhproject.domain.BoardEntity;
import lombok.Builder;
import lombok.Getter;


@Getter
public class BoardResponseDto {

    private final Long boardId;
    private final String title;
    private final String content;



    @Builder
    public BoardResponseDto(
            Long boardId, String title, String content) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;



    }

    public static BoardResponseDto fromEntity(BoardEntity board) {
        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }
}

