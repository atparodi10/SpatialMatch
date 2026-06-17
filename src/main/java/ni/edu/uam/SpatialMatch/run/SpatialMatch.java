package ni.edu.uam.SpatialMatch.run;

import org.openxava.util.*;

/**
 * Ejecuta esta clase para arrancar la aplicación.
 */

public class SpatialMatch {

	public static void main(String[] args) throws Exception {
		// DBServer.start("SpatialMatch-db"); // Para usar tu propia base de datos comenta esta línea y configura src/main/webapp/META-INF/context.xml
		AppServer.run("SpatialMatch"); // Usa AppServer.run("") para funcionar en el contexto raíz
	}

}
