package ni.edu.uam.SpatialMatch.modelo;

import java.math.*;
import java.time.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.model.*;

import lombok.*;

@Entity @Getter @Setter
public class Usuario extends Identifiable {
	
	@Column(length=50) @Required
	String descripcion;
	
	LocalDate fecha;
	
	BigDecimal importe;
	
}
