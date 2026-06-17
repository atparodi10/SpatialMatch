package ni.edu.uam.SpatialMatch.acciones;

import ni.edu.uam.SpatialMatch.modelo.Pregunta;
import org.apache.commons.lang3.reflect.Typed;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.SpatialMatch.modelo.Evaluacion;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class IniciarEvaluacionAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        LocalDateTime horaInicioPrueba = LocalDateTime.now();

        // Creamos la instancia de la evaluación en la base de datos
        Evaluacion nuevaEvaluacion = new Evaluacion();
        XPersistence.getManager().persist(nuevaEvaluacion);

        // LÓGICA DE INICIO: Buscar la primera pregunta (Número 1)
        TypedQuery<Pregunta> query = XPersistence.getManager().createQuery(
                "SELECT p FROM Pregunta p WHERE p.numeroPregunta = 1", Pregunta.class
        );
        List<Pregunta> resultados = query.getResultList();

        if(resultados.isEmpty()){
            addError("Error: no hay preguntas configuradas en la base de datos");
            return;
        }
        Pregunta primeraPregunta = resultados.get(0);

        // LÓGICA DE VISTA: La vista del estudiante
        // [FRONT: En esta parte OpenXava descarta la vista anterior y carga la "PantallaPrueba"]
        getView().setValue("evaluacion.id", nuevaEvaluacion.getOid());
        getView().setValue("pregunta.id", primeraPregunta.getId());

        //cronómetros ocultos
        getView().setValue("horaInicioTemporal",  horaInicioPrueba);
        // Se guarda el inicio general para calcular el tiempo total al finalizar.
        getView().setValue("HoraInicioTemporalPruebaGeneral", horaInicioPrueba);


        addMessage("La prueba de Figuras Idénticas ha comenzado.");
    }
}