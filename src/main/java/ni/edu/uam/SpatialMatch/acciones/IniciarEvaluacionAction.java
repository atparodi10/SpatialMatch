package ni.edu.uam.SpatialMatch.acciones;

import ni.edu.uam.SpatialMatch.modelo.Pregunta;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.SpatialMatch.modelo.Evaluacion;
import ni.edu.uam.SpatialMatch.modelo.Usuario;

import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class IniciarEvaluacionAction extends ViewBaseAction {

    private static final String EVALUACION_ACTIVA_ID = "evaluacionActivaId";
    private static final String PREGUNTA_ACTIVA_ID = "preguntaActivaId";

    @Override
    public void execute() throws Exception {
        Date horaInicioPrueba = new Date();

        String idUsuario = getView().getValueString("usuario.oid");
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            addError("Debe seleccionar un usuario antes de iniciar la prueba.");
            return;
        }

        Usuario usuario = XPersistence.getManager().find(Usuario.class, idUsuario);
        if (usuario == null) {
            addError("No se encontro el usuario seleccionado en la base de datos.");
            return;
        }

        TypedQuery<Pregunta> query = XPersistence.getManager().createQuery(
                "SELECT p FROM Pregunta p ORDER BY p.numeroPregunta", Pregunta.class
        );
        List<Pregunta> preguntas = query.getResultList();

        if (preguntas.isEmpty()) {
            addError("No hay preguntas configuradas en la base de datos. Por favor, agregue preguntas primero.");
            return;
        }

        Collections.shuffle(preguntas);

        List<String> preguntasPendientes = preguntas.stream()
                .map(Pregunta::getId)
                .collect(Collectors.toList());

        Evaluacion evaluacion = obtenerOcrearEvaluacion(usuario);
        XPersistence.getManager().flush();

        String primerId = preguntasPendientes.remove(0);
        Pregunta primeraPregunta = XPersistence.getManager().find(Pregunta.class, primerId);

        showDialog();
        getView().setModelName("RegistroRespuesta");
        getView().setViewName("PantallaPrueba");

        getView().putObject(EVALUACION_ACTIVA_ID, evaluacion.getOid());
        getView().putObject(PREGUNTA_ACTIVA_ID, primerId);
        getView().putObject("preguntasPendientes", preguntasPendientes);
        getView().putObject("horaInicioPregunta", horaInicioPrueba);
        getView().putObject("horaInicioTemporalPruebaGeneral", horaInicioPrueba);

        cargarPreguntaEnVista(primeraPregunta);
        getView().setValue("opcionSeleccionada", null);

        setControllers("PruebaActivaController");

        addMessage("La prueba de Figuras Identicas ha comenzado. Responda cada ejercicio y presione Siguiente.");
    }

    private Evaluacion obtenerOcrearEvaluacion(Usuario usuario) throws Exception {
        String idEvaluacionActual = null;
        try {
            idEvaluacionActual = getView().getValueString("oid");
        } catch (Exception ignored) {
            // La accion normalmente inicia desde Evaluacion. Si no existe oid visible,
            // se crea una evaluacion nueva de forma controlada.
        }

        Evaluacion evaluacion = null;
        if (idEvaluacionActual != null && !idEvaluacionActual.trim().isEmpty()) {
            evaluacion = XPersistence.getManager().find(Evaluacion.class, idEvaluacionActual);
        }

        if (evaluacion == null) {
            evaluacion = new Evaluacion();
            evaluacion.setFechaCreacion(java.time.LocalDate.now());
            evaluacion.setUsuario(usuario);
            XPersistence.getManager().persist(evaluacion);
        } else {
            evaluacion.setUsuario(usuario);
            XPersistence.getManager().merge(evaluacion);
        }

        return evaluacion;
    }

    private void cargarPreguntaEnVista(Pregunta pregunta) throws Exception {
        if (pregunta == null) {
            addError("No se pudo cargar la pregunta inicial de la prueba.");
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
