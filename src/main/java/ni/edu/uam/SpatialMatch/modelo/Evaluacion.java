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
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
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
    @ReadOnly
    private String retroalimentacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @DescriptionsList
    private Usuario usuario;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL)
    private Collection<RegistroRespuesta> registroRespuestas;


    // Metodos

    public int calcularPuntaje(){
        int puntajeTemporal = 0;
        if(this.registroRespuestas != null){
            for(RegistroRespuesta respuesta : this.registroRespuestas){
                if(respuesta.isEsCorrecta()){
                    puntajeTemporal++;
                }
            }
        }
        this.puntajeFinal = puntajeTemporal;
        return this.puntajeFinal;
    }


    public String generarRetroalimentacion(){
        if(this.registroRespuestas == null || this.registroRespuestas.isEmpty()){
            this.retroalimentacion = "Error: La evaluación no ha concluido correctamente o no posee respuestas registradas para generar retroalimentación.";
            return this.retroalimentacion;
        }

        int totalPreguntas = this.registroRespuestas.size();
        double porcentajeAciertos = ((double) this.puntajeFinal / totalPreguntas) * 100;

        if(porcentajeAciertos >= 90){
            this.retroalimentacion = "Desempeño Sobresaliente. El usuario demuestra una excelente aptitud espacial y reconocimiento visual de figuras idénticas.";
        }

        else if(porcentajeAciertos >= 70 ){
            this.retroalimentacion = "Desempeño Satisfactorio. El usuario posee un nivel adecuado de aptitud espacial, logrando identificar la mayoría de los patrones correctamente.";
        }

        else if(porcentajeAciertos >= 50){
            this.retroalimentacion = "Desempeño Promedio. El usuario presenta habilidades espaciales básicas, pero se identifican áreas de oportunidad en el reconocimiento rápido de figuras.";
        }

        else {
            this.retroalimentacion = "Desempeño Bajo. Se detectan dificultades significativas en la aptitud espacial. Se sugiere realizar ejercicios de reforzamiento visual.";
        }
        return this.retroalimentacion;

    }





}
