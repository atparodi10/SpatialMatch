package ni.edu.uam.SpatialMatch.acciones;

import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.SpatialMatch.modelo.RegistroRespuesta;
import ni.edu.uam.SpatialMatch.modelo.Evaluacion;
import ni.edu.uam.SpatialMatch.modelo.Pregunta;
import java.time.LocalDateTime;

public class SiguientePreguntaAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        LocalDateTime horaClic = LocalDateTime.now();

        String opcionSeleccionada = getView().getValueString("opcionSeleccionada");
        LocalDateTime horaInicioPregunta = (LocalDateTime) getView().getValue("horaInicioTemporal");

        String idEvaluacion = getView().getValueString("evaluacion.id");
        String idPreguntaActual = getView().getValueString("pregunta.id");

        Evaluacion evaluacion = XPersistence.getManager().find(Evaluacion.class, idEvaluacion);
        Pregunta preguntaActual = XPersistence.getManager().find(Pregunta.class, idPreguntaActual);

        RegistroRespuesta registro = new RegistroRespuesta();
        registro.setEvaluacion(evaluacion);
        registro.setPregunta(preguntaActual);
        registro.setOpcionSeleccionada(opcionSeleccionada);
        registro.setHoraInicio(horaInicioPregunta);
        registro.setHoraRespuesta(horaClic);

        XPersistence.getManager().persist(registro);

        // (Logica para cargar la siguiente pregunta a la vista)

        getView().setValue("opcionSeleccionada", "");
        getView().setValue("horaInicioTemporal", horaClic);
    }
}