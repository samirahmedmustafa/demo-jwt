package com.example.entity;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.AppService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class Controller {
	
	private final AppService appService;
	
	@GetMapping("users/{id}")
	public ResponseEntity<User> findUser(@PathVariable Long id) {
		User user = appService.findUser(id);
		log.info("findUser(Long id): {}", user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@GetMapping("roles/{id}")
	public ResponseEntity<Role> findRole(@PathVariable Long id) {
		Role role = appService.findRole(id);
		log.info("findRole(Long id): {}", role);
		return new ResponseEntity<>(role, HttpStatus.OK);
	}
	
	@GetMapping("roles")
	public ResponseEntity<List<Role>> findRoles() {
		List<Role> roles = appService.findRoles();
		log.info("findRoles(): {}", roles);
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}
	
	@GetMapping("users")
	public ResponseEntity<List<User>> findUsers() {
		List<User> users = appService.findUsers();
		log.info("findUsers(): {}", users);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@PostMapping("users")
	public ResponseEntity<User> saveUser(@RequestBody User user) {
		User saved = appService.saveUser(user);
		log.info("saveUser(User user): {}");
		return new ResponseEntity<User>(saved, HttpStatus.CREATED);
	}
	
	@PostMapping("roles")
	public ResponseEntity<Role> saveRole(@RequestBody Role role) {
		Role saved = appService.saveRole(role);
		log.info("saveRole(Role role): {}");
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}
	
	@PutMapping("users")
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		User saved = appService.saveUser(user);
		log.info("updateUser(User user): {}");
		return new ResponseEntity<User>(saved, HttpStatus.CREATED);
	}
	
	@PutMapping("roles")
	public ResponseEntity<Role> updateRole(@RequestBody Role role) {
		Role saved = appService.saveRole(role);
		log.info("updateRole(Role role): {}");
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}
	
	@DeleteMapping("users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		appService.removeUser(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("roles/{id}")
	public ResponseEntity<?> deleteRole(@PathVariable Long id) {
		appService.removeRole(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("users/addUserRole")
	public ResponseEntity<?> addUserRole(@RequestBody UserRoleForm userRoleForm)	{
		appService.addUserRole(userRoleForm.getUsername(), userRoleForm.getRoleName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("users/removeUserRole")
	public ResponseEntity<?> removeUserRole(@RequestBody UserRoleForm userRoleForm)	{
		appService.removeUserRole(userRoleForm.getUsername(), userRoleForm.getRoleName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

@Data
class UserRoleForm {
	private String username;
	private String roleName;
}
