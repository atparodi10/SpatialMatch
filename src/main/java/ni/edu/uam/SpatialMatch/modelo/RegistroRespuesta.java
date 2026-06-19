package ni.edu.uam.SpatialMatch.modelo;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter
@View(
        name = "PantallaPrueba",
        members =
                "Informacion del Ejercicio [" +
                        "   pregunta.numeroPregunta;" +
                        "   pregunta.identificadorFigura;" +
                        "];" +
                        "Tu Respuesta [" +
                        "   opcionSeleccionada" +
                        "]"
)
public class RegistroRespuesta {

    public enum OpcionValida {
        A, B, C, D, E
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @Hidden
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(length = 5)
    @Required(message = "Debes seleccionar una opción antes de continuar")
    private OpcionValida opcionSeleccionada;

    @Column
    private boolean esCorrecta;

    @Column
    private LocalDateTime horaInicio;

    @Column
    private LocalDateTime horaRespuesta;

    @ManyToOne(fetch = FetchType.LAZY)
    private Evaluacion evaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @NoCreate @NoModify @NoSearch
    private Pregunta pregunta;

    @PrePersist
    @PreUpdate
    private void evaluarRespuestaAutomatica() {
        if (this.pregunta != null && this.opcionSeleccionada != null) {
            this.esCorrecta = this.pregunta.validarRespuesta(this.opcionSeleccionada.name());
        } else {
            this.esCorrecta = false;
        }
    }

    public long calcularSegundosInvertidos() {
        if (this.horaInicio != null && this.horaRespuesta != null) {
            return ChronoUnit.SECONDS.between(this.horaInicio, this.horaRespuesta);
        }
        return 0;
    }
}