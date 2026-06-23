package ni.edu.uam.SpatialMatch.modelo;

import java.security.MessageDigest;
import java.util.Collection;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import lombok.*;

@Entity
@Getter
@Setter
@Tab(properties = "nombre, apellido, correo, rol, sesionActiva")
public class Usuario {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@Hidden
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 36)
	private String oid;

	@Column(length = 50)
	@Required
	private String nombre;

	@Column(length = 50)
	@Required
	private String apellido;

	@Column(length = 100, unique = true)
	@Required
	@Stereotype("EMAIL")
	private String correo;

	@Column(length = 64)
	@Required
	@Stereotype("PASSWORD")
	private String password;

	public enum RolUsuario {
		ADMIN, ENCUESTADO
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	@Required
	private RolUsuario rol;

	@Column
	@ReadOnly
	private boolean sesionActiva = false;

	@OneToMany(mappedBy = "usuario")
	@ListProperties("fechaCreacion, puntajeFinal, tiempoTotalPrueba, retroalimentacion")
	private Collection<Evaluacion> evaluaciones;

	// LOGICA DE SEGURIDAD Y ENCRIPTACION

	/**
	 * Convierte una contrasena en texto plano a un Hash seguro SHA-256
	 */
	private String generarHash(String textoPlano) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(textoPlano.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException("Error al encriptar la contrasena", ex);
		}
	}