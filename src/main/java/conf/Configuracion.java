package conf;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Configuracion {

	
	public static String urlData = "https://www.ninjabet.es/get_data_sp.php";
	public static String urlEvents = "https://www.ninjabet.es/get_events_sp.php";
	public static String urlExchange = "https://ero.betfair.es/www/sports/exchange/readonly/v1/bymarket";

	public static Integer FiltroMinutosAntiguedad = 20;
	public static Double restaCuotaCodere = 0.05;

	public static String uid = "e7dfca01f394755c11f853602cb2608a";
	public static String ratingInicial = "92";
	public static String ratingNivel1 = "95";
	public static String ratingNivel2 = "92";
	public static String cuotaMinimaInicial = "2.5";
	public static String cuotaMinima = "2.5";
	public static String cuotaNivel1 = "2.5";
	public static String cuotaNivel2 = "5";

	public static Double nCuotaMinima = Double.valueOf(cuotaMinima);
	public static Double ratingNivel1Minimo = Double.valueOf(ratingNivel1);
	public static Double ratingNivel2Minimo = Double.valueOf(ratingNivel2);

	public static final String CSV_FILE = "C:" + File.separator + "BOT" + File.separator + "CONF" + File.separator + "oddsAnteriores.csv";

	public static ArrayList<String> filtroBookies2UP = new ArrayList<String>(Arrays.asList("2", "48", "7", "39", "69"));
	public static ArrayList<String> filtroBookies2UP2WAY = new ArrayList<String>(			Arrays.asList("2", "75", "48", "7", "39", "69", "47"));
	public static ArrayList<String> filtroBookiesVacio = new ArrayList<>();
	public static ArrayList<String> filtroApuestas2UP = new ArrayList<String>(Arrays.asList("home", "away"));
	public static ArrayList<String> filtroApuestasHome = new ArrayList<String>(Arrays.asList("home"));
	public static ArrayList<String> filtroApuestasDraw = new ArrayList<String>(Arrays.asList("draw"));
	public static ArrayList<String> filtroApuestasAway = new ArrayList<String>(Arrays.asList("away"));

	
	
}
