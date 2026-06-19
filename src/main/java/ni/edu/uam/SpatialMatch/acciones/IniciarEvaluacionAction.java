package ni.edu.uam.SpatialMatch.acciones;

import ni.edu.uam.SpatialMatch.modelo.Pregunta;
import org.apache.commons.lang3.reflect.Typed;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import ni.edu.uam.SpatialMatch.modelo.Evaluacion;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class IniciarEvaluacionAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        LocalDateTime horaInicioPrueba = LocalDateTime.now();

        // Creamos la instancia de la evaluación en la base de datos
        Evaluacion nuevaEvaluacion = new Evaluacion();
        XPersistence.getManager().persist(nuevaEvaluacion);

        // LÓGICA DE INICIO: Buscar la primera pregunta (Número 1)
        TypedQuery<Pregunta> query = XPersistence.getManager().createQuery(
                "SELECT p FROM Pregunta p", Pregunta.class
        );
        List<Pregunta> resultados = query.getResultList();

        if(resultados.isEmpty()){
            addError("Error: no hay preguntas configuradas en la base de datos");
            return;
        }
        Collections.shuffle(resultados);

        // Extraemos solo los IDs para guardarlos fácilmente en memoria
        List<String> preguntasPendientes = resultados.stream()
                .map(Pregunta::getId)
                .collect(Collectors.toList());

        // Sacar la primera pregunta de la baraja para mostrarla de inmediato
        String primerId = preguntasPendientes.remove(0);

        // 4. Guardar variables vitales en la memoria de la sesión de este usuario
        getView().putObject("preguntasPendientes", preguntasPendientes);
        // Cronómetro para la pregunta individual:
        getView().putObject("horaInicioPregunta", horaInicioPrueba);
        // Cronómetro para toda la evaluación completa (Vital para tu FinalizarEvaluacionAction):
        getView().putObject("horaInicioTemporalPruebaGeneral", horaInicioPrueba);

        // 5. Cambiar la pantalla para mostrar la pregunta
        showDialog();
        getView().setModelName("RegistroRespuesta");

        // Cargar los datos en la pantalla (¡Manteniendo tus conexiones originales!)
        getView().setValue("evaluacion.oid", nuevaEvaluacion.getOid());
        getView().setValue("pregunta.id", primerId);

        // Ocultar botones por defecto para usar botones personalizados
        setControllers("PruebaActivaController");

        // Mensaje de bienvenida que tú tenías
        addMessage("La prueba de Figuras Idénticas ha comenzado.");
    }
}