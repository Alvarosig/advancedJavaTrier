package br.com.trier.spring.models;

import br.com.trier.spring.models.dto.PaisDTO;
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
@EqualsAndHashCode (of = "id")
@Entity (name = "pais")
public class Pais {
    
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_pais")
    private Integer id;
    
    @Column (name = "nome_pais", unique = true)
    private String nameCountry;
    
    public Pais (PaisDTO dto) {
        this(dto.getId(), dto.getNameCountry());
    }
    
    public PaisDTO toDTO() {
        return new PaisDTO(id, nameCountry);
    }
}
