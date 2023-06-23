package br.com.trier.spring.models;

import br.com.trier.spring.models.dto.TeamDTO;
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
@Entity(name = "team")
public class Team {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer id;

    @Column(name = "team_name", unique = true)
    private String teamName;

    public Team(TeamDTO dto) {
        this(dto.getId(), dto.getTeamName());
    }

    public TeamDTO toDTO() {
        return new TeamDTO(id, getTeamName());
    }
}
