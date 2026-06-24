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

    }
}
