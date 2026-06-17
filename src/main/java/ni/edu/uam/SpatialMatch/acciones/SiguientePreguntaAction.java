package ni.edu.uam.SpatialMatch.acciones;

import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.SpatialMatch.modelo.RegistroRespuesta;
import ni.edu.uam.SpatialMatch.modelo.Evaluacion;
import ni.edu.uam.SpatialMatch.modelo.Pregunta;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

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

        LocalDateTime horaInicioPregunta = (LocalDateTime) getView().getValue("horaInicioTemporal");


        String idEvaluacion = getView().getValueString("evaluacion.id");
        String idPreguntaActual = getView().getValueString("pregunta.id");

        Evaluacion evaluacion = XPersistence.getManager().find(Evaluacion.class, idEvaluacion);
        Pregunta preguntaActual = XPersistence.getManager().find(Pregunta.class, idPreguntaActual);


        RegistroRespuesta registro = new RegistroRespuesta();
        registro.setEvaluacion(evaluacion);
        registro.setPregunta(preguntaActual);
        registro.setOpcionSeleccionada(opcionEnum);
        registro.setHoraInicio(horaInicioPregunta);
        registro.setHoraRespuesta(horaClic);

        XPersistence.getManager().persist(registro);

        // (Lógica para cargar la siguiente pregunta a la vista)

        int numeroSiguiente = preguntaActual.getNumeroPregunta();

        TypedQuery<Pregunta> query = XPersistence.getManager().createQuery(
                "SELECT p from Pregunta p WHERE p.numeroPregunta = :numero", Pregunta.class
        );
        query.setParameter("numero", numeroSiguiente);

        List<Pregunta> resultados = query.getResultList();

        if(!resultados.isEmpty()){
            Pregunta siguientePregunta = resultados.get(0);

            // [FRONT: Aquí OpenXava recarga la imagen de la nueva matriz en el navegador]
            getView().setValue("pregunta.id", siguientePregunta.getId());

            // Limpiamos el ComboBox para que la nueva figura esté sin responder
            getView().setValue("opcionSeleccionada", null);

            // Reiniciamos el cronómetro interno para la nueva pregunta
            getView().setValue("horaInicioTemporal", horaClic);
        }
        else {
            // SI YA NO HAY MÁS PREGUNTAS (FIN DE LA PRUEBA):
            // [FRONT: Evento para mostrar pantalla final o HTML de resultados]
            addMessage("¡Has respondido todas las preguntas! Prueba completada.");
        }
    }
}