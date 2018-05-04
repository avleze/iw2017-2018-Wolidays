package es.uca.wolidays.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.Usuario;
import es.uca.wolidays.backend.repositories.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService{
	@Autowired
	private UsuarioRepository repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = repo.findByUsername(username);
		if (usuario == null) {
			throw new UsernameNotFoundException(username);
		}
		return usuario;
	}
	
	public Usuario loadUserByUsernameWithApartamentos(String username) throws UsernameNotFoundException {
		Usuario usuario = repo.findByUsernameWithApartamentos(username);
		if (usuario == null) {
			throw new UsernameNotFoundException(username);
		}
		return usuario;
	}
	
	public Usuario loadUserByUsernameWithReservas(String username) throws UsernameNotFoundException {
		Usuario usuario = repo.findByUsernameWithReservas(username);
		if (usuario == null) {
			throw new UsernameNotFoundException(username);
		}
		return usuario;
	}
	
	public Usuario guardar(Usuario usuario) {
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword() != null ? usuario.getPassword() : "default"));
		return repo.save(usuario);	
	}
	
	public Optional<Usuario> buscarPorId(Integer pk) {
		return repo.findById(pk);
	}

	public void eliminar(Usuario usuario) {
		repo.delete(usuario);
	}

	public Iterable<Usuario> usuarios() {
		return repo.findAll();
	}
}
