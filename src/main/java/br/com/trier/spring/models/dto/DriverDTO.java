package br.com.trier.spring.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    
    private Integer id;
    private String name;
    private String teamName;
    private Integer teamId;
    private String countryName;
    private Integer countryId;
    
}
