package com.example.dhproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ApiResponseDto {

    private final boolean success;
    private final String message;
    private final Object data;

}
