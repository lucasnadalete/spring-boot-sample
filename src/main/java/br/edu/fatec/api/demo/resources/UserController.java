package br.edu.fatec.api.demo.resources;

import br.edu.fatec.api.demo.entity.User;
import br.edu.fatec.api.demo.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@Api(value = "User")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all users")
    public ResponseEntity<List<User>> listAll() {
        List<User> listUsers = userRepository.findAll();
        if (!listUsers.isEmpty())
            return new ResponseEntity<>(listUsers, HttpStatus.OK);
        else
            return new ResponseEntity<>(listUsers, HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Register a new user")
    public ResponseEntity<User> save(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
