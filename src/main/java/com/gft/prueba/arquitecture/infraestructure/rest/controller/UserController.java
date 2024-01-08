package com.gft.prueba.arquitecture.infraestructure.rest.controller;

import com.gft.prueba.arquitecture.application.usecases.UserService;
import com.gft.prueba.arquitecture.domain.model.dto.UserDto;
import com.gft.prueba.arquitecture.domain.model.dto.request.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable long id){
        return userService.getById(id);
    }

    @Operation(
            operationId = "UserGet",
            summary = "Get user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful response", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
                    })
            }
    )
    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PostMapping()
    public UserDto create(@RequestBody UserRequest taskRequest){
        return userService.createNew(taskRequest);
    }

    @PutMapping("/{id}")
    public UserDto userEdit(@RequestBody UserRequest taskRequest,
                            @PathVariable Long id){
        return userService.update(taskRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id){
        userService.deleteById(id);
    }

    @PostMapping("/{id}/tasks")
    public void assignTasks(@PathVariable Long id , @RequestParam List<Long> tasksIds){
        userService.assignTasks(id, tasksIds);
    }

}
