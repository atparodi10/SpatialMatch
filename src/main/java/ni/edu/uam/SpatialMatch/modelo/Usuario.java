package ni.edu.uam.SpatialMatch.modelo;

import java.math.*;
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

	public boolean iniciarSesion(String correo, String password){
		if(correo == null || password == null || correo.trim().isEmpty() || password.trim().isEmpty()){
			throw new IllegalArgumentException("Error: Alguno de los campos requeridos se encuentra vacío.");
		}

		if(!this.correo.equalsIgnoreCase(correo.trim())){
			throw new SecurityException("Error: El correo electrónico proporcionado no coincide con el registro.");
		}

		if(!this.password.equals(password)){
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
