package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;
    private final UserMapperIdEmail userMapperIdEmail;

    @GetMapping
    public List<UserDto> getAllUsers() {

        log.info("log.info -> userService.findAllUsers");

        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::toDto)
                          .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable final Long id) {
        return userService.getUser(id);

    }

    // curl -X GET "http://localhost:8080/v1/users/simple"
    @GetMapping("/simple")
    public List<UserDtoSimple> getAllUsersSimple() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::toDtoSimple)
                          .collect(Collectors.toList());
    }


    // curl -X GET "http://localhost:8080/v1/users/email?email=domain"
    @GetMapping("/email")
    public List<UserDtoIdEmail> getUserByEmail(@RequestParam final String email) {

        log.info("log.info -> @GetMapping(/email  {}", email);

        return userService.getUsersByEmail(email)
                          .stream()
                          .map(userMapperIdEmail::toDtoIdEmail)
                          .collect(Collectors.toList());
    }

    // curl -X GET "http://localhost:8080/v1/users/older-than?age=60"
    @GetMapping("/older-than")
    public List<UserDto> getUsersOlderThan(@RequestParam  final int age) {

        //int age = Integer.MAX_VALUE;
        //try{
        //    age = Integer.parseInt(ageS);
        //}
        //catch (NumberFormatException ignored){}

        log.info("log.info -> @GetMapping(/older-than?age={}", age);

        return userService.getUsersOlderThan(age)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }


    // curl -X GET "http://localhost:8080/v1/users/older-than2/60"
    @GetMapping("/older-than2/{age}")
    public List<UserDto> getUsersOlderThan2(@PathVariable final int age) {

        log.info("log.info -> @GetMapping(/older-than2/{}", age);

        return userService.getUsersOlderThan(age)
                          .stream()
                          .map(userMapper::toDto)
                          .collect(Collectors.toList());
    }

    // Windows curl -X POST "http://localhost:8080/v1/users" -H "Content-Type: application/json" -d "{\"firstName\": \"firsName\", \"lastName\": \"lastName\", \"birthdate\": \"1990-10-11\", \"email\": \"example@example.com\"}"
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User>  addUser(@RequestBody UserDto userDto) {
        User newUser = userService.createUser(
                userMapper.toEntity(userDto));
        URI location = URI.create("/v1/users/" + newUser.getId());
        return ResponseEntity.created(location).body(newUser);
    }

    // Windows curl -X PUT  http://localhost:8080/v1/users/11 -H "Content-Type: application/json" -d "{\"firstName\": \"newFirstName\", \"lastName\": \"newLastName\", \"birthdate\": \"2000-02-03\", \"email\": \"new@example.com\"}"
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable final Long id, @RequestBody UserDto userDto) {
        User updatedUser = userService.updateUser(id, userMapper.toEntity(userDto));
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    // Windows curl -X DELETE http://localhost:8080/v1/users/11
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void>  delUser(@PathVariable final Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
