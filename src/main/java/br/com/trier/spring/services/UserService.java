package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.models.User;

public interface UserService {

    User findById (Integer id);
    
    User insert(User user);
    
    List<User> listAll ();
    
    User update (User user);
    
    void delete (Integer id);
    
    List<User> findByNameStartingWithIgnoreCase(String name);

    
}
