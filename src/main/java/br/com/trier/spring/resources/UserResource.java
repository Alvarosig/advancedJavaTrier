package br.com.trier.spring.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.spring.models.User;
import br.com.trier.spring.services.UserService;

@RestController
@RequestMapping(value = "/usuarios")
public class UserResource {

    @Autowired
    private UserService service;
    
    @PostMapping
    public ResponseEntity<User> insert (@RequestBody User user) {
        User newUser = service.insert(user);
        return ResponseEntity.ok(newUser);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> findById (@PathVariable Integer id) {
        User user = service.findById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping
    public ResponseEntity<List<User>> listAll () {
        List<User> lista = service.listAll();
        return ResponseEntity.ok(lista);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> update (@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        return ResponseEntity.ok(service.update(user));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<List<User>> findByNameStartingWithIgnoreCase (@PathVariable String name) {
        List<User> lista = service.findByNameStartingWithIgnoreCase(name);
        return ResponseEntity.ok(lista);
    }
    
}
