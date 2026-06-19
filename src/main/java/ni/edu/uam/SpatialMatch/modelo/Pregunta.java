package ni.edu.uam.SpatialMatch.modelo;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pregunta {

    public enum OpcionValida {
        A, B, C, D, E
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @Hidden
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;

    @Min(value = 1, message = "El número mínimo de pregunta es 1")
    @Max(value = 20, message = "El número máximo de preguntas es 20")
    @Column(nullable = false, unique = true)
    private int numeroPregunta;

    @Column(length = 255)
    @Required
    private String identificadorFigura;

    @Enumerated(EnumType.STRING)
    @Required
    private OpcionValida respuestaCorrecta;

    /**
     * Valida si la opción proporcionada por el usuario coincide con la respuesta correcta.
     */
    public boolean validarRespuesta(String respuestaUsuario) {
        if (respuestaUsuario == null || this.respuestaCorrecta == null) {
            return false;
        }
        return this.respuestaCorrecta.name().equalsIgnoreCase(respuestaUsuario.trim());
    }
}