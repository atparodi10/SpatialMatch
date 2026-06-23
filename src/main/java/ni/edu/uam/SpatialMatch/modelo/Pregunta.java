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
@Views({
        @View(
                members =
                        "numeroPregunta;" +
                                "identificadorFigura;" +
                                "imagenFigura;" +
                                "opciones [#" +
                                "   imagenOpcionA, imagenOpcionB, imagenOpcionC;" +
                                "   imagenOpcionD" +
                                "];" +
                                "respuestaCorrecta"
        ),
        @View(
                name = "ParaPrueba",
                members =
                        "numeroPregunta;" +
                                "identificadorFigura;" +
                                "imagenFigura;" +
                                "opciones [#" +
                                "   imagenOpcionA, imagenOpcionB, imagenOpcionC;" +
                                "   imagenOpcionD" +
                                "]"
        )
})
public class Pregunta {

    public enum OpcionValida {
        A, B, C, D
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @Hidden
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;

    @Min(value = 1, message = "El numero minimo de pregunta es 1")
    @Max(value = 20, message = "El numero maximo de preguntas es 20")
    @Column(nullable = false, unique = true)
    private int numeroPregunta;

    @Column(length = 100)
    @Required
    private String identificadorFigura;

    @Stereotype("PHOTO")
    @Lob
    private byte[] imagenFigura;

    @Stereotype("PHOTO")
    @Lob
    private byte[] imagenOpcionA;

    @Stereotype("PHOTO")
    @Lob
    private byte[] imagenOpcionB;

    @Stereotype("PHOTO")
    @Lob
    private byte[] imagenOpcionC;

    @Stereotype("PHOTO")
    @Lob
    private byte[] imagenOpcionD;

    @Enumerated(EnumType.STRING)
    @Required
    private OpcionValida respuestaCorrecta;

    /**
     * Valida si la opcion proporcionada por el usuario coincide con la respuesta correcta.
     */
    public boolean validarRespuesta(String respuestaUsuario) {
        if (respuestaUsuario == null || this.respuestaCorrecta == null) {
            return false;
        }
        return this.respuestaCorrecta.name().equalsIgnoreCase(respuestaUsuario.trim());
    }

    @Override
    public String toString() {
        return numeroPregunta + " - " + (identificadorFigura != null ? identificadorFigura : "");
    }
}