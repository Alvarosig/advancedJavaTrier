package br.com.trier.spring.models;

import br.com.trier.spring.models.dto.ChampionshipDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "championship")
public class Championship {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "championship_id")
    private Integer id;

    @Column(name = "champ_description")
    private String champDesc;

    @Column(name = "champ_year")
    private Integer year;

    public Championship(ChampionshipDTO dto) {
        this(dto.getId(), dto.getChampDesc(), dto.getYear());
    }

    public ChampionshipDTO toDTO() {
        return new ChampionshipDTO(id, getChampDesc(), getYear());
    }
}