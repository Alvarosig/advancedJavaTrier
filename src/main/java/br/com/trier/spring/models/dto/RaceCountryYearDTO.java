package br.com.trier.spring.models.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RaceCountryYearDTO {

    private Integer year;
    private String country;
    private Integer raceSize;
    private List<RaceDTO> races;
}
