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
