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

}
