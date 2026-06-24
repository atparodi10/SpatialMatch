package ni.edu.uam.SpatialMatch.acciones;

import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.SpatialMatch.modelo.Evaluacion;

import java.util.Date;

public class FinalizarEvaluacionAction extends ViewBaseAction {

    private static final String EVALUACION_ACTIVA_ID = "evaluacionActivaId";

    @Override
    public void execute() throws Exception {
        Date horaFinalPrueba = new Date();
        Date horaInicioPrueba = (Date) getView().getObject("horaInicioTemporalPruebaGeneral");

        String idEvaluacion = obtenerObjetoComoTexto(EVALUACION_ACTIVA_ID);
        if (idEvaluacion == null) {
            addError("No se encontro la evaluacion activa.");
            closeDialog();
            return;


        }

        Evaluacion evaluacion = XPersistence.getManager().find(Evaluacion.class, idEvaluacion);

        if (evaluacion == null) {
            addError("No se pudo encontrar la evaluacion en la base de datos.");
            closeDialog();
            return;
        }
        if (horaInicioPrueba != null) {
            long tiempoTotal = (horaFinalPrueba.getTime() - horaInicioPrueba.getTime()) / 1000;
            evaluacion.setTiempoTotalPrueba(tiempoTotal);
        }

        Long puntosTotales = XPersistence.getManager().createQuery(
                "SELECT COUNT(r) FROM RegistroRespuesta r WHERE r.evaluacion.oid = :idEvaluacion AND r.esCorrecta = true",
                Long.class
        ).setParameter("idEvaluacion", idEvaluacion).getSingleResult();

        evaluacion.setPuntajeFinal(puntosTotales.intValue());

        evaluacion.generarRetroalimentacion();

        XPersistence.getManager().merge(evaluacion);
        XPersistence.getManager().flush();

        addMessage("Prueba finalizada con exito. Puntaje obtenido: " + puntosTotales + " respuestas correctas.");
        closeDialog();


    }

