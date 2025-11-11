package ru.practicum.main.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}