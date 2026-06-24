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
}

