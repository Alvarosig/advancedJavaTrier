package br.com.trier.spring.models;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode (of = "id")
@Entity (name = "corrida")
public class Corrida {
	
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_pais")
    private Integer id;
	
	@Column (name = "data_corrida")
    private ZonedDateTime date;
	
	@ManyToOne
	private Pista pista;
	
	@ManyToOne
	private Campeonato campeonato;
}
