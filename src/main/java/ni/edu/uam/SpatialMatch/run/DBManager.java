package ni.edu.uam.SpatialMatch.run;

import org.openxava.util.*;

/**
 * Ejecuta esta clase para iniciar un gestor para tu base de datos de desarrollo.
 * NOTA: Solo es util si usas la base de datos embebida HSQLDB.
 * Con PostgreSQL externo no es necesario ejecutar esta clase.
 */
public class DBManager {

	public static void main(String[] args) {
		DBServer.runManager();
	}

}
