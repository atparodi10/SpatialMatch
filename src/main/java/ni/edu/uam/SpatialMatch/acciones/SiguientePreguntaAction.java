package ni.edu.uam.SpatialMatch.acciones;

import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.SpatialMatch.modelo.RegistroRespuesta;
import ni.edu.uam.SpatialMatch.modelo.Evaluacion;
import ni.edu.uam.SpatialMatch.modelo.Pregunta;

import java.time.LocalDateTime;
import java.util.List;

public class SiguientePreguntaAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {

        LocalDateTime horaClic = LocalDateTime.now();

        // Extraer la opción seleccionada de la vista (tu lógica estricta)
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

        // Recuperar IDs y reloj usando los nombres definidos en IniciarEvaluacionAction
        // (Cambios en nombre de variables 'horaInicioTemporal' por 'horaInicioPregunta')
        LocalDateTime horaInicioPregunta = (LocalDateTime) getView().getObject("horaInicioPregunta");

        String idEvaluacion = getView().getValueString("evaluacion.oid");
        String idPreguntaActual = getView().getValueString("pregunta.id");

        Evaluacion evaluacion = XPersistence.getManager().find(Evaluacion.class, idEvaluacion);
        Pregunta preguntaActual = XPersistence.getManager().find(Pregunta.class, idPreguntaActual);

        // Guardar el registro manualmente
        RegistroRespuesta registro = new RegistroRespuesta();
        registro.setEvaluacion(evaluacion);
        registro.setPregunta(preguntaActual);
        registro.setOpcionSeleccionada(opcionEnum);
        registro.setHoraInicio(horaInicioPregunta);
        registro.setHoraRespuesta(horaClic);

        XPersistence.getManager().persist(registro);

        // ==========================================================
        // 4. LÓGICA ALEATORIA PARA LA SIGUIENTE PREGUNTA
        // ==========================================================

        @SuppressWarnings("unchecked")
        List<String> pendientes = (List<String>) getView().getObject("preguntasPendientes");

        if (pendientes != null && !pendientes.isEmpty()) {
            // Tomamos la siguiente pregunta aleatoria de la baraja
            String siguienteId = pendientes.remove(0);

            // Actualizamos la baraja en memoria y el reloj para la nueva pregunta
            getView().putObject("preguntasPendientes", pendientes);
            getView().putObject("horaInicioPregunta", horaClic);

            // [FRONT: Aquí OpenXava recarga la imagen de la nueva matriz en el navegador]
            getView().setValue("pregunta.id", siguienteId);

            // Limpiamos el ComboBox para que la nueva figura esté sin responder
            getView().setValue("opcionSeleccionada", null);

        } else {
            // SI YA NO HAY MÁS PREGUNTAS (FIN DE LA PRUEBA):
            addMessage("¡Has respondido todas las preguntas! Prueba completada.");
            closeDialog();
        }
    }
}