package com.example.demo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.tomcat.jni.User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public Iterable<Users> all() {
        return this.repository.findAll();
    }

    @PostMapping("")
    public Users create(@RequestBody Users user) {
        return this.repository.save(user);
    }

    @GetMapping("/{id}")
    public Optional<Users> getById(@PathVariable Long id) {
        return this.repository.findById(id);
    }

    @PatchMapping("/{id}")
    public Users updateById(@PathVariable Long id, @RequestBody Users user) throws ObjectNotFoundException {

        Optional<Users> foundUser = this.repository.findById(id);

        if(foundUser.isPresent()) {
            Users updatedUser = foundUser.get();
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            return this.repository.save(user);
        } else {
          throw new ObjectNotFoundException("Could not find lesson id");
        }
    }

    @DeleteMapping("/{id}")
    public Object deleteById(@PathVariable Long id) {

        this.repository.deleteById(id);
        UserDeleteCount users = new UserDeleteCount();
        users.setCount(this.repository.count());
        return users;
    }


    @PostMapping("/authenticate")
    public Object getAuthenticated(@RequestBody Users user) {

         Users foundUser = this.repository.findByEmail(user.getEmail());
         Authenticated userPassed = new Authenticated();
         UserA userFailed = new UserA();

         if(user.getPassword().equals(foundUser.getPassword())) {
             userPassed.setAuthenticated(true);
             userPassed.setUser(foundUser);
             return userPassed;
         } else {
             userFailed.setAuthenticated(false);
             return userFailed;

         }

    }
}
