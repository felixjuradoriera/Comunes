package utils;

import conf.Configuracion;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DatosPruebasUtils {
	
	private static final String JSON_FILE = Configuracion.BASE_DIR + File.separator + "datosPruebas.json";
	
	public static void guardarJsonEnArchivo(StringBuilder json) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(JSON_FILE))) {
	        writer.write(json.toString());
	        System.out.println("✅ JSON guardado en: " + JSON_FILE);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
}
