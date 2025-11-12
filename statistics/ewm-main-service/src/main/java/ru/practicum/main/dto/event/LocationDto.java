package ru.practicum.main.dto.event;

import lombok.*;

/* # Координаты события (DTO) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    private Double lat; // # широта
    private Double lon; // # долгота
}