package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.*;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.CurrentUser;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS', 'USER_DS')")
    @GetMapping(path = "")
    public UserInfoDto getUser(@CurrentUser final UserPrinciple currentUser) {
        return userService.getUserByUsername(currentUser.getUsername());
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @GetMapping(path = "/{username}")
    public UserInfoDto getUserByUsername(@PathVariable("username") final String username) {
        return userService.getUserByUsername(username);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS', 'USER_DS')")
    @GetMapping(path = "/carrier")
    public List<UserInfoDto> getUserListWithSameCarrier() {
        return userService.getUserListWithSameCarrier();
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @GetMapping(path = "/names")
    public List<String> getUsers() {
        return userService.getUsers();
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @GetMapping(path = "/name/{username}")
    public boolean isUserExist(@PathVariable("username") final String username) {
        return userService.isUsernameExist(username);
    }

    @PreAuthorize("hasAnyRole('USER_DS', 'ADMIN_DS')")
    @GetMapping(path = "/list")
    public List<UserInfoDto> getUserList() {
        return userService.getUserList();
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS')")
    @DeleteMapping(path = "")
    public UserInfoDto deleteUser(@CurrentUser final UserPrinciple currentUser) {
        return userService.deleteUser(currentUser.getId());
    }

    @PreAuthorize("hasAnyRole('ADMIN_DS')")
    @DeleteMapping(path = "/{id}")
    public UserInfoDto deleteUserById(@PathVariable("id") final Long id) {
        return userService.deleteUser(id);
    }
}
