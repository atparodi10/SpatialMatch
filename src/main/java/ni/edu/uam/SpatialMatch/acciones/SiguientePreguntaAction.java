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

        Object valorVista = getView().getValue("opcionSeleccionada");
        RegistroRespuesta.OpcionValida opcionEnum = null;

        if (valorVista != null) {
            if (valorVista instanceof Integer) {
                // Si OpenXava devuelve el índice numérico (0=A, 1=B, etc.)
                opcionEnum = RegistroRespuesta.OpcionValida.values()[(Integer) valorVista];
            } else {
                // Por seguridad, si lo devuelve como texto directo
                opcionEnum = RegistroRespuesta.OpcionValida.valueOf(valorVista.toString());
            }
        }
        // --------------------------------------------------

        LocalDateTime horaInicioPregunta = (LocalDateTime) getView().getValue("horaInicioTemporal");

        String idEvaluacion = getView().getValueString("evaluacion.id");
        String idPreguntaActual = getView().getValueString("pregunta.id");

        Evaluacion evaluacion = XPersistence.getManager().find(Evaluacion.class, idEvaluacion);
        Pregunta preguntaActual = XPersistence.getManager().find(Pregunta.class, idPreguntaActual);

        RegistroRespuesta registro = new RegistroRespuesta();
        registro.setEvaluacion(evaluacion);
        registro.setPregunta(preguntaActual);

        // Asignamos la variable ya convertida correctamente a Enum
        registro.setOpcionSeleccionada(opcionEnum);

        registro.setHoraInicio(horaInicioPregunta);
        registro.setHoraRespuesta(horaClic);

        XPersistence.getManager().persist(registro);

        // (Lógica para cargar la siguiente pregunta a la vista)

        // Limpiamos la pantalla y reiniciamos el cronómetro
        getView().setValue("opcionSeleccionada", "");
        getView().setValue("horaInicioTemporal", horaClic);
    }
}