package com.example.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.RoleRepo;
import com.example.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AppService implements UserDetailsService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepo userRepo;
	private final RoleRepo roleRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.info("loadUserByUsername authenticating User: {}", username);
		
		User user = userRepo.findByUsername(username);
		if(user == null) {
			log.error("loadUserByUsername User not found");
			throw new UsernameNotFoundException("loadUserByUsername User not found");
		}
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		
		user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}

	public User findUser(Long id) {
		User user = null;
		try {
			user = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("Invalid user id: " + id));
		} catch (Exception e) {
			log.error("findUser(Long id) Exception: {}", e.getMessage());
		}
		return user;
	}
	
	public Role findRole(Long id) {
		Role role = null;
		try {
			role = roleRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("Invalid role id: " + id));
		} catch (Exception e) {
			log.error("findRole(Long id) Exception: {}", e.getMessage());
		}
		return role;
	}
	
	public List<Role> findRoles() {
		List<Role> roles = roleRepo.findAll();
		log.info("findRoles(): {}", roles);
		return roles;
	}
	
	public List<User> findUsers() {
		List<User> users = userRepo.findAll();
		log.info("findUsers(): {}", users);
		return users;
	}
	
	public User saveUser(User item) {
		item.setPassword(passwordEncoder.encode(item.getPassword()));
		User saved = userRepo.save(item);
		log.info("saveUser(User item): {}", saved);
		return saved;
	}
	
	public User updateUser(User item) {
		if(item.getPassword() != null && !item.getPassword().trim().equals(""))	item.setPassword(passwordEncoder.encode(item.getPassword()));
		User saved = userRepo.save(item);
		log.info("saveUser(User item): {}", saved);
		return saved;
	}
	
	public Role saveRole(Role item) {
		Role saved = roleRepo.save(item);
		log.info("saveRole(Role item): {}", saved);
		return saved;
	}
	
	public void removeRole(Long id) {
		log.info("removeRole(Long id): {}", id);
		roleRepo.deleteById(id);
	}
	
	public void removeUser(Long id) {
		log.info("removeUser(Long id): {}", id);
		userRepo.deleteById(id);
	}
	
	public void addUserRole(String username, String roleName) {
		User user = userRepo.findByUsername(username);
		Role role = roleRepo.findByName(roleName);
		user.getRoles().add(role);
	}
	
	public void removeUserRole(String username, String roleName) {
		User user = userRepo.findByUsername(username);
		Role role = roleRepo.findByName(roleName);
		user.getRoles().remove(role);
	}
}
