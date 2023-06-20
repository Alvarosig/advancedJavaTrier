package br.com.trier.spring.models;

import br.com.trier.spring.models.dto.UserDTO;
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
@Entity (name = "usuario")
public class User {
    
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_usuario")
    private Integer id;
    
    @Column (name = "nome_usuario")
    private String name;
    
    @Column (name = "email_usuario", unique = true)
    private String email;
    
    @Column (name = "senha_usuario")
    private String password;
    
    public User (UserDTO dto) {
        this(dto.getId(), dto.getName(), dto.getEmail(), dto.getPassword());
    }
    
    public UserDTO toDTO() {
        return new UserDTO(id, name, email, password);
    }
}
