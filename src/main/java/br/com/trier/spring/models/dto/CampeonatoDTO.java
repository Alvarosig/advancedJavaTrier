package br.com.trier.spring.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CampeonatoDTO {

	private Integer id;
	private String champDesc;
	private Integer year;
}
