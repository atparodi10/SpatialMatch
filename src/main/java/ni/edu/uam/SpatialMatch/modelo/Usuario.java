package ni.edu.uam.SpatialMatch.modelo;

import java.math.*;
import java.security.MessageDigest;
import java.time.*;
import java.util.Collection;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import org.openxava.model.*;

import lombok.*;

@Entity @Getter @Setter
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

	@Column(length = 50)
	@Required
	@Stereotype("EMAIL")
	private String correo;

	@Column(length = 64)
	@Required
	@Stereotype("PASSWORD")
	private String password;

	@Column(length = 50)
	@Required
	private String rol;

	@Column
	@ReadOnly // Se bloquea en la interfaz para que el administrador no lo cambie manualmente
	private boolean sesionActiva = false;

	@OneToMany(mappedBy = "usuario")
	@ListProperties("fechaCreacion, puntajeFinal, tiempoTotalPrueba")
	private Collection<Evaluacion> evaluaciones;

	// LÓGICA DE SEGURIDAD Y ENCRIPTACIÓN

	/**
	 * Convierte una contraseña en texto plano a un Hash seguro SHA-256
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
			throw new RuntimeException("Error al encriptar la contraseña", ex);
		}
	}

	/**
	 * Interceptor de JPA: Se ejecuta automáticamente antes de GUARDAR o ACTUALIZAR.
	 */
	@PrePersist
	@PreUpdate
	private void encriptarAntesDeGuardar() {
		// Solo encriptamos si la contraseña no tiene 64 caracteres
		// (Esto evita que se encripte doble vez si el admin actualiza otro dato como el nombre)
		if (this.password != null && this.password.length() != 64) {
			this.password = generarHash(this.password);
		}
	}

	public boolean iniciarSesion(String correo, String passwordEnPlano){
		if(correo == null || passwordEnPlano == null || correo.trim().isEmpty() || passwordEnPlano.trim().isEmpty()){
			throw new IllegalArgumentException("Error: Alguno de los campos requeridos se encuentra vacío.");
		}

		if(!this.correo.equalsIgnoreCase(correo.trim())){
			throw new SecurityException("Error: El correo electrónico proporcionado no coincide con el registro.");
		}

		// Hasheamos la contraseña que el usuario escribió para ver si coincide con la BD
		String intentoHash = generarHash(passwordEnPlano);

		if(!this.password.equals(intentoHash)){
			throw new SecurityException("Error: La contraseña ingresada es incorrecta.");
		}
		return true;
	}

	public void cerrarSesion(){
		if(this.sesionActiva){
			this.sesionActiva = false;
		}

		else{
			throw new IllegalStateException("Error: El usuario ya se encuentra desconectado.");
		}
	}

}
