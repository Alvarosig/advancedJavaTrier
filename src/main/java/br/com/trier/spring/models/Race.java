package br.com.trier.spring.models;

import java.time.ZonedDateTime;

import br.com.trier.spring.models.dto.RaceDTO;
import br.com.trier.spring.utils.DateUtils;
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
@EqualsAndHashCode(of = "id")
@Entity(name = "race")
public class Race {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "race_id")
    private Integer id;

    @Column(name = "race_date")
    private ZonedDateTime date;

    @ManyToOne
    private Track track;

    @ManyToOne
    private Championship championship;

    public Race(RaceDTO raceDTO, Track track, Championship championship) {
        this(raceDTO.getId(), DateUtils.strToZoneDateTime(raceDTO.getDate()), track, championship);
    }

    public RaceDTO toDTO() {
        return new RaceDTO(id, DateUtils.zonedDateTimeToStr(date), track.getId(), track.getName(), championship.getId(), championship.getChampDesc());
    }
}
