package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.controller;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.RoleDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.UserEditDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.UserPasswordDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.dto.UserUsernameDto;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.CurrentUser;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.service.UserPrinciple;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    public ResponseEntity<?> getUser(@CurrentUser final UserPrinciple currentUser) {
        return userService.getUserResponseByUsername(currentUser.getUsername());
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @GetMapping(path = "/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable("username") final String username) {
        return userService.getUserResponseByUsername(username);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS', 'USER_DS')")
    @GetMapping(path = "/carrier")
    public ResponseEntity<?> getUserListWithSameCarrier(@CurrentUser final UserPrinciple currentUser) {
        return userService.getUserListWithSameCarrier(currentUser.getUsername());
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @GetMapping(path = "/names")
    public ResponseEntity<List<String>> getUsers() {
        return userService.getUsers();
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @GetMapping(path = "/name/{username}")
    public ResponseEntity<?> isUserExist(@PathVariable("username") final String username) {
        return userService.isUsernameExist(username);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @GetMapping(path = "/list")
    public ResponseEntity<?> getUserList(@CurrentUser final UserPrinciple currentUser) {
        return userService.getUserList(currentUser);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS', 'USER_DS')")
    @PutMapping(path = "")
    public ResponseEntity<?> updateOther(@CurrentUser final UserPrinciple currentUser, @Validated @RequestBody final UserEditDto userRequest) {
        return userService.updateOther(currentUser.getUsername(), userRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @PutMapping(path = "/{username}")
    public ResponseEntity<?> updateOtherCurrentUser(@PathVariable("username") final String username, @Validated @RequestBody final UserEditDto userRequest) {
        return userService.updateOther(username, userRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS', 'USER_DS')")
    @PutMapping(path = "/updatepassword")
    public ResponseEntity<?> updatePassword(@CurrentUser final UserPrinciple currentUser, @Validated @RequestBody final UserPasswordDto passwordRequest) {
        return userService.updatePassword(currentUser.getUsername(), passwordRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @PutMapping(path = "/updatepassword/{username}")
    public ResponseEntity<?> updateCurrentUserPassword(@PathVariable("username") final String username, @Validated @RequestBody final UserPasswordDto passwordRequest) {
        return userService.updatePassword(username, passwordRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS', 'USER_DS')")
    @PutMapping(path = "/updatename")
    public ResponseEntity<?> updateName(@CurrentUser final UserPrinciple currentUser, @Validated @RequestBody final UserUsernameDto nameRequest) {
        return userService.updateUsername(currentUser.getUsername(), nameRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @PutMapping(path = "/updatename/{username}")
    public ResponseEntity<?> updateNameCurrentUser(@PathVariable("username") final String username, @Validated @RequestBody final UserUsernameDto nameRequest) {
        return userService.updateUsername(username, nameRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @DeleteMapping(path = "")
    public ResponseEntity<?> deleteUser(@CurrentUser final UserPrinciple currentUser) {
        return userService.deleteUser(currentUser.getId());
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") final Long id) {
        return userService.deleteUser(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ')")
    @PostMapping(path = "/addrole/{username}")
    public ResponseEntity<?> addRole(@PathVariable("username") final String username, @Validated @RequestBody final RoleDto roleDto) {
        return userService.addRole(username, roleDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ', 'ADMIN_DS')")
    @PutMapping(path = "/role/{username}")
    public ResponseEntity<?> updateUserRole(@CurrentUser final UserPrinciple currentUser, @PathVariable("username") final String username,
                                            @Validated @RequestBody final RoleDto roleDto) {
        return userService.updateUserRole(currentUser, username, roleDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN_SZ')")
    @DeleteMapping(path = "/removerole/{username}")
    public ResponseEntity<?> removeRole(@PathVariable("username") final String username, @Validated @RequestBody final RoleDto roleDto) {
        return userService.addRole(username, roleDto);
    }
}
