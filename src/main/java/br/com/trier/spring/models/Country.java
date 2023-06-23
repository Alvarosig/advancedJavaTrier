package br.com.trier.spring.models;

import br.com.trier.spring.models.dto.CountryDTO;
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
@Entity(name = "country")
public class Country {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Integer id;

    @Column(name = "country_name", unique = true)
    private String name;

    public Country(CountryDTO dto) {
        this(dto.getId(), dto.getCountryName());
    }

    public CountryDTO toDTO() {
        return new CountryDTO(id, name);
    }
}
