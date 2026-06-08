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
public class Evaluacion {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @Hidden
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(length = 36)
    private String oid;

    @Required
    @DefaultValueCalculator(CurrentLocalDateCalculator.class)
    private LocalDate fechaCreacion;

    @Column
    private int puntajeFinal;

    @Column
    private long tiempoTotalPrueba;

    @Column(columnDefinition = "TEXT")
    @Stereotype("MEMO")
    private String retroalimentacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @DescriptionsList
    private Usuario usuario;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL)
    private Collection<RegistroRespuesta> registroRespuestas;

}
