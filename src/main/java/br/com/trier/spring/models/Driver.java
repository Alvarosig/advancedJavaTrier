package br.com.trier.spring.models;

import br.com.trier.spring.models.dto.DriverDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "piloto")
public class Driver {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_piloto")
    private Integer id;

    @Column(name = "nome_piloto")
    private String name;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Country country;

    public Driver(DriverDTO driverDTO, Team team, Country country) {
        this(driverDTO.getId(), driverDTO.getName(), team, country);
    }

    public DriverDTO toDTO() {
        return new DriverDTO(id, name, team.getTeamName(), team.getId(), country.getName(), country.getId());
    }
}
