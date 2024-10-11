package com.example.dhproject.dto.request;

import com.example.dhproject.domain.BoardEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class BoardRequestPostDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Builder
    public BoardRequestPostDto(String title, String content) {

        this.title = title;
        this.content = content;
    }

    public static BoardEntity toEntity(String title, String content) {

        return BoardEntity.builder()
                .title(title)
                .content(content)
                .build();
    }
}
