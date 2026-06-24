package ni.edu.uam.SpatialMatch.modelo;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter
@View(
        name = "PantallaPrueba",
        members =
                "Informacion del Ejercicio [" +
                        "   pregunta;" +
                        "];" +
                        "Tu Respuesta [" +
                        "   opcionSeleccionada" +
                        "]"
)
public class RegistroRespuesta {
    public enum OpcionValida {
        A, B, C, D
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @Hidden
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(length = 5)
    @Required(message = "Debes seleccionar una opcion antes de continuar")
    @Editor(forViews = "PantallaPrueba", value = "ValidValuesRadioButton")
    private OpcionValida opcionSeleccionada;

}