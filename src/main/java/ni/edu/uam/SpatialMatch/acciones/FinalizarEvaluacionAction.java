package ni.edu.uam.SpatialMatch.acciones;

import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.SpatialMatch.modelo.Evaluacion;
import ni.edu.uam.SpatialMatch.modelo.RegistroRespuesta;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

public class FinalizarEvaluacionAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        LocalDateTime horaFinalPrueba = LocalDateTime.now();
        LocalDateTime horaInicioPrueba = (LocalDateTime) getView().getValue("horaInicioTemporalPruebaGeneral");

        String idEvaluacion = getView().getValueString("evaluacion.Oid");
        Evaluacion evaluacion = XPersistence.getManager().find(Evaluacion.class, idEvaluacion);

        // Calcular el tiempo total de la prueba
        if (horaInicioPrueba != null) {
            long tiempoTotal = ChronoUnit.SECONDS.between(horaInicioPrueba, horaFinalPrueba);
            evaluacion.setTiempoTotalPrueba(tiempoTotal);
        }

        // Calcular el puntaje final sumando los registros
        int puntosTotales = 0;
        Collection<RegistroRespuesta> registros = evaluacion.getRegistroRespuestas();
        if (registros != null) {
            for (RegistroRespuesta respuesta : registros) {
                if (respuesta.isEsCorrecta()) {
                    puntosTotales++;
                }
            }
        }
        evaluacion.setPuntajeFinal(puntosTotales);

        evaluacion.generarRetroalimentacion();

        // Guardamos los cálculos finales
        XPersistence.getManager().merge(evaluacion);

        // [FRONTEND: Aquí se puede redirigir al estudiante a un HTML de "Resultados"
        // o mostrar una ventana emergente (modal) con su puntaje final]

        addMessage("Prueba finalizada con éxito. Respuestas enviadas.");
    }
}