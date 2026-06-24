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
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Hidden
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String oid;

    @Required
    @DefaultValueCalculator(CurrentLocalDateCalculator.class)
    private LocalDate fechaCreacion;

    @Column
    @ReadOnly
    private int puntajeFinal;

    @Column
    @ReadOnly
    private long tiempoTotalPrueba;

    @Column(columnDefinition = "TEXT")
    @Stereotype("MEMO")
    @ReadOnly
    private String retroalimentacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @DescriptionsList(descriptionProperties = "nombre, apellido")
    @Required
    private Usuario usuario;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL)
    @ListProperties("pregunta.numeroPregunta, pregunta.identificadorFigura, opcionSeleccionada, resultadoCorreccion, horaInicio, horaRespuesta")
    private Collection<RegistroRespuesta> registroRespuestas;

    public int calcularPuntaje() {
        int puntajeTemporal = 0;
        if (this.registroRespuestas != null) {
            for (RegistroRespuesta respuesta : this.registroRespuestas) {
                if (respuesta.isEsCorrecta()) {
                    puntajeTemporal++;
                }
            }
        }
        this.puntajeFinal = puntajeTemporal;
        return this.puntajeFinal;
    }

    public String generarRetroalimentacion() {
        if (this.registroRespuestas == null || this.registroRespuestas.isEmpty()) {
            this.retroalimentacion = "Error: La evaluacion no ha concluido correctamente o no posee respuestas registradas.";
            return this.retroalimentacion;
        }

        int totalPreguntas = this.registroRespuestas.size();
        double porcentajeAciertos = ((double) this.puntajeFinal / totalPreguntas) * 100;

        if (porcentajeAciertos >= 90) {
            this.retroalimentacion = "Rendimiento Sobresaliente. El usuario demuestra una excelente aptitud espacial y reconocimiento visual de figuras identicas.";
        } else if (porcentajeAciertos >= 70) {
            this.retroalimentacion = "Rendimiento Satisfactorio. El usuario posee un nivel adecuado de aptitud espacial, logrando identificar la mayoria de los patrones correctamente.";
        } else if (porcentajeAciertos >= 50) {
            this.retroalimentacion = "Rendimiento Promedio. El usuario presenta habilidades espaciales basicas, pero se identifican areas de oportunidad en el reconocimiento rapido de figuras.";
        } else {
            this.retroalimentacion = "Rendimiento Bajo. Se detectan dificultades significativas en la aptitud espacial. Se sugiere realizar ejercicios de reforzamiento visual.";
        }
        return this.retroalimentacion;
    }
}
