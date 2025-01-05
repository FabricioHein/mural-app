package com.bezkoder.spring.security.postgresql.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.bezkoder.spring.security.postgresql.models.WeddingData;
import com.bezkoder.spring.security.postgresql.repository.WeddingDataRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.security.postgresql.models.ERole;
import com.bezkoder.spring.security.postgresql.models.Role;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.LoginRequest;
import com.bezkoder.spring.security.postgresql.payload.request.SignupRequest;
import com.bezkoder.spring.security.postgresql.payload.response.JwtResponse;
import com.bezkoder.spring.security.postgresql.repository.RoleRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private WeddingDataRepository weddingDataRepository;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      // Autenticar usuário com email e senha
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);

      // Obter detalhes do usuário autenticado
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      List<String> roles = userDetails.getAuthorities().stream()
              .map(item -> item.getAuthority())
              .collect(Collectors.toList());

      // Buscar ou criar o WeddingData associado ao usuário
      WeddingData wedding = weddingDataRepository.findByUserId(userDetails.getId())
              .orElseGet(WeddingData::new);

      // Retornar resposta de sucesso
      return ResponseEntity.ok(new JwtResponse(
              jwt,
              userDetails.getId(),
              userDetails.getUsername(),
              userDetails.getEmail(),
              roles,
              wedding
      ));
    } catch (UsernameNotFoundException ex) {
      // Tratativa para usuário não encontrado
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(Collections.singletonMap("message", "Error: Não encontrado usuário com esse usuario: " + loginRequest.getUsername()));
    } catch (BadCredentialsException ex) {
      // Tratativa para credenciais inválidas
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(Collections.singletonMap("message", "Error: usuario ou Senha Inválidos!"));
    } catch (Exception ex) {
      // Tratativa para erros gerais
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(Collections.singletonMap("message", "Error: Ocorreu um erro inesperado. Tente novamente mais tarde."));
    }
  }


  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    try {
      // Verificar se o email já está em uso
      if (userRepository.existsByEmail(signUpRequest.getEmail())) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", "Error: O e-mail já está em uso!"));
      }

      // Criar a conta do novo usuário
      User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
              encoder.encode(signUpRequest.getPassword()));

      Set<String> strRoles = signUpRequest.getRole();
      Set<Role> roles = new HashSet<>();

      if (strRoles == null) {
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Default role USER not found in the database."));
        roles.add(userRole);
      } else {
        for (String role : strRoles) {
          Role foundRole;
          switch (role.toLowerCase()) {
            case "admin":
              foundRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                      .orElseThrow(() -> new EntityNotFoundException("Admin role not found in the database."));
              break;
            case "mod":
              foundRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                      .orElseThrow(() -> new EntityNotFoundException("Moderator role not found in the database."));
              break;
            default:
              foundRole = roleRepository.findByName(ERole.ROLE_USER)
                      .orElseThrow(() -> new EntityNotFoundException("Default role USER not found in the database."));
          }
          roles.add(foundRole);
        }
      }

      user.setRoles(roles);
      userRepository.save(user);

      return ResponseEntity.status(HttpStatus.CREATED)
              .body(Collections.singletonMap("message", "Usuário cadastrado com sucesso!"));

    } catch (EntityNotFoundException ex) {
      // Erros relacionados a papéis não encontrados
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(Collections.singletonMap("message", "Error: " + ex.getMessage()));
    } catch (Exception ex) {
      // Tratativa para erros inesperados
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(Collections.singletonMap("message", "Error: Ocorreu um erro inesperado. Tente novamente mais tarde."));
    }
  }



}
