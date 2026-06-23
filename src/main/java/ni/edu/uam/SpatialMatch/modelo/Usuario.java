package ni.edu.uam.SpatialMatch.modelo;

import java.math.*;
import java.time.*;
import java.util.Collection;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import org.openxava.model.*;

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

	@Column(length = 50)
	@Required
	private String rol;

	@OneToMany(mappedBy = "usuario")
	@ListProperties("fechaCreacion, puntajeFinal, tiempoTotalPrueba, retroalimentacion")
	private Collection<Evaluacion> evaluaciones;

	
}
