
package ni.edu.uam.SpatialMatch.acciones;

import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.SpatialMatch.modelo.RegistroRespuesta;
import ni.edu.uam.SpatialMatch.modelo.Evaluacion;
import ni.edu.uam.SpatialMatch.modelo.Pregunta;

import java.util.Date;
import java.util.List;

public class SiguientePreguntaAction extends ViewBaseAction {

    private static final String EVALUACION_ACTIVA_ID = "evaluacionActivaId";
    private static final String PREGUNTA_ACTIVA_ID = "preguntaActivaId";

    @Override
    public void execute() throws Exception {

        Date horaClic = new Date();

        Object valorVista = getView().getValue("opcionSeleccionada");
        if (valorVista == null || valorVista.toString().trim().isEmpty()) {
            addError("Debes seleccionar una opcion antes de continuar.");
            return;
        }

        RegistroRespuesta.OpcionValida opcionEnum;
        try {
            if (valorVista instanceof Integer) {
                opcionEnum = RegistroRespuesta.OpcionValida.values()[(Integer) valorVista];
            } else {
                opcionEnum = RegistroRespuesta.OpcionValida.valueOf(valorVista.toString().trim().toUpperCase());
            }
        } catch (Exception e) {
            addError("Opcion seleccionada no valida: " + valorVista);
            return;
        }

        Date horaInicioPregunta = (Date) getView().getObject("horaInicioPregunta");
        String idEvaluacion = obtenerObjetoComoTexto(EVALUACION_ACTIVA_ID);
        String idPreguntaActual = obtenerObjetoComoTexto(PREGUNTA_ACTIVA_ID);

        if (idEvaluacion == null || idPreguntaActual == null) {
            addError("Error interno: No se encontro la evaluacion o la pregunta activa.");
            return;
        }

        Evaluacion evaluacion = XPersistence.getManager().find(Evaluacion.class, idEvaluacion);
        Pregunta preguntaActual = XPersistence.getManager().find(Pregunta.class, idPreguntaActual);

        if (evaluacion == null || preguntaActual == null) {
            addError("Error interno: Datos de evaluacion no encontrados en base de datos.");
            return;
        }

        RegistroRespuesta registro = new RegistroRespuesta();
        registro.setEvaluacion(evaluacion);
        registro.setPregunta(preguntaActual);
        registro.setOpcionSeleccionada(opcionEnum);
        registro.setHoraInicio(horaInicioPregunta != null ? horaInicioPregunta : horaClic);
        registro.setHoraRespuesta(horaClic);

        XPersistence.getManager().persist(registro);
        XPersistence.getManager().flush();

        @SuppressWarnings("unchecked")
        List<String> pendientes = (List<String>) getView().getObject("preguntasPendientes");

        if (pendientes != null && !pendientes.isEmpty()) {
            String siguienteId = pendientes.remove(0);
            Pregunta siguientePregunta = XPersistence.getManager().find(Pregunta.class, siguienteId);

            getView().putObject("preguntasPendientes", pendientes);
            getView().putObject("horaInicioPregunta", horaClic);
            getView().putObject(PREGUNTA_ACTIVA_ID, siguienteId);

            cargarPreguntaEnVista(siguientePregunta);
            getView().setValue("opcionSeleccionada", null);

            addMessage("Respuesta registrada. Siguiente pregunta cargada.");
        } else {
            executeAction("PruebaActivaController.finalizarPrueba");
        }
    }

    private String obtenerObjetoComoTexto(String clave) {
        Object valor = getView().getObject(clave);
        if (valor == null || valor.toString().trim().isEmpty()) {
            return null;
        }
        return valor.toString();
    }

    private void cargarPreguntaEnVista(Pregunta pregunta) throws Exception {
        if (pregunta == null) {
            addError("No se pudo cargar la siguiente pregunta de la prueba.");
            return;
        }

        getView().setValue("pregunta.id", pregunta.getId());
        getView().setValue("pregunta.numeroPregunta", pregunta.getNumeroPregunta());
        getView().setValue("pregunta.identificadorFigura", pregunta.getIdentificadorFigura());
        getView().setValue("pregunta.imagenFigura", pregunta.getImagenFigura());
        getView().setValue("pregunta.imagenOpcionA", pregunta.getImagenOpcionA());
        getView().setValue("pregunta.imagenOpcionB", pregunta.getImagenOpcionB());
        getView().setValue("pregunta.imagenOpcionC", pregunta.getImagenOpcionC());
        getView().setValue("pregunta.imagenOpcionD", pregunta.getImagenOpcionD());
        getView().setEditable("pregunta.numeroPregunta", false);
        getView().setEditable("pregunta.identificadorFigura", false);
        getView().setEditable("pregunta.imagenFigura", false);
        getView().setEditable("pregunta.imagenOpcionA", false);
        getView().setEditable("pregunta.imagenOpcionB", false);
        getView().setEditable("pregunta.imagenOpcionC", false);
        getView().setEditable("pregunta.imagenOpcionD", false);
    }
}
