package ni.edu.uam.SpatialMatch.run;

import org.openxava.util.*;

/**
 * Ejecuta esta clase para arrancar la aplicacion SpatialMatch.
 * La aplicacion usa PostgreSQL configurada en META-INF/context.xml
 */
public class  SpatialMatch {

	public static void main(String[] args) throws Exception {
		// Para usar PostgreSQL externa, se comenta DBServer.start y se usa solo AppServer.run
		AppServer.run("SpatialMatch");
	}

}
