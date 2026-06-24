package ni.edu.uam.SpatialMatch.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentLocalDateCalculator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Getter
@Setter
@Tab(properties = "fechaCreacion, usuario.nombre, usuario.apellido, puntajeFinal, tiempoTotalPrueba")
@View(members =
        "fechaCreacion;" +
                "puntajeFinal;" +
                "tiempoTotalPrueba;" +
                "retroalimentacion;" +
                "usuario;" +
                "registroRespuestas"
)
public class Evaluacion {

}
