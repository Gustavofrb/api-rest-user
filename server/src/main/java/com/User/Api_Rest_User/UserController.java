package com.User.Api_Rest_User;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
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

import Security.SecurityDto.Login;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final UsernamePasswordAuthenticationToken authenticate = null;
	@Autowired
	private UserRepository repository;
	
	@GetMapping
	public List<User> findAll(){
	    return repository.findAll();
	}
	
	@GetMapping(value = "/{id}")
	public User findById(@PathVariable Long id){
            return repository.findById(id).get();
            
	}
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping("/login")
	public String login(@RequestBody Login login) {
	    try {
	        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
	            new UsernamePasswordAuthenticationToken(login.login(), login.password());
	        
	        Authentication authenticate = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
	        var user = (User) authenticate.getPrincipal();
	        return tokenService.gerarToken(user);
	    
	    
	 } catch (AuthenticationException e) {
	        throw new BadCredentialsException("Usuário ou senha inválidos");
	    } finally {}
	}

        @PostMapping
        public ResponseEntity<Object> insert(@RequestBody User user) {
        	
            try {
                user.name = BCrypt.hashpw(user.name, BCrypt.gensalt(7));
                repository.save(user);
                return new ResponseEntity<Object>("Sucess:true", HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<Object>("Erro ao salvar usuário: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @PutMapping(value = "/{id}")
	public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody User user) {
            Optional<User> optionalUser = repository.findById(id);
            if (!optionalUser.isPresent()) {
                    return ResponseEntity.notFound().build();
            }
            if(user.password != null) {
                user.password = BCrypt.hashpw(user.password, BCrypt.gensalt(7));
            }
            user.id = id;
            repository.save(user);
            return ResponseEntity.ok(user);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<User> delete(@PathVariable("id") Long id) {
            Optional<User> optionalUser = repository.findById(id);
            if (!optionalUser.isPresent()) {
                    return ResponseEntity.notFound().build();
            }
            repository.deleteById(id);
            return ResponseEntity.ok().build();
	}

}
