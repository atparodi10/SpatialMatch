package ni.edu.uam.SpatialMatch.modelo;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pregunta {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @Hidden
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;

    @Column
    @Required
    private int numeroPregunta;

    @Lob
    @Stereotype("PHOTO")
    private byte[] imagenMatriz;

    @Column(length = 255)
    @Required
    private String respuestaCorrecta;

    /**
     * Valida si la opción proporcionada por el usuario coincide con la respuesta correcta.
     */
    public boolean validarRespuesta(String respuestaUsuario) {
        if (respuestaUsuario == null || this.respuestaCorrecta == null) {
            return false;
        }
        return this.respuestaCorrecta.equalsIgnoreCase(respuestaUsuario.trim());
    }
}