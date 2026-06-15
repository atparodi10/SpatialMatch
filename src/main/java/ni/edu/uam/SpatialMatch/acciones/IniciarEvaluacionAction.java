package ni.edu.uam.SpatialMatch.acciones;

import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.SpatialMatch.modelo.Evaluacion;
import java.time.LocalDateTime;

public class IniciarEvaluacionAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        LocalDateTime horaInicioPrueba = LocalDateTime.now();

        // Creamos la instancia de la evaluación en la base de datos
        Evaluacion nuevaEvaluacion = new Evaluacion();
        XPersistence.getManager().persist(nuevaEvaluacion);

        // Guardamos los datos de la evaluación y el cronómetro oculto en la vista
        getView().setValue("evaluacion.id", nuevaEvaluacion.getOid());
        getView().setValue("horaInicioTemporal", horaInicioPrueba);

        // (Logica de inicio de prueba)

        addMessage("La prueba de Figuras Idénticas ha comenzado.");
    }
}