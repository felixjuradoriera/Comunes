package conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class Configuracion {

	// ============================================================
	// Directorio base de configuración/persistencia.
	// Se puede sobreescribir con la variable de entorno BOT_CONF_DIR
	// (necesario en Linux/Raspberry Pi). Si no se define, se usa
	// C:\BOT\CONF en Windows y /opt/bots/CONF en cualquier otro SO.
	// ============================================================
	public static final String BASE_DIR = resolveBaseDir();

	private static String resolveBaseDir() {
		String override = System.getenv("BOT_CONF_DIR");
		if (override != null && !override.isBlank()) {
			return override;
		}
		String os = System.getProperty("os.name", "").toLowerCase();
		if (os.contains("win")) {
			return "C:" + File.separator + "BOT" + File.separator + "CONF";
		}
		return File.separator + "opt" + File.separator + "bots" + File.separator + "CONF";
	}

	// ============================================================
	// Secrets cargados desde BASE_DIR/bot.properties
	// NUNCA hardcodear tokens aquí.
	// ============================================================
	private static final Properties secrets = new Properties();
	private static final String SECRETS_FILE = BASE_DIR + File.separator + "bot.properties";

	static {
		File f = new File(SECRETS_FILE);
		if (!f.exists()) {
			throw new ExceptionInInitializerError(
				"[Configuracion] No se encontró el fichero de secrets: " + SECRETS_FILE +
				"\nCopia bot.properties.example a esa ruta y rellena los valores."
			);
		}
		try (FileInputStream fis = new FileInputStream(f)) {
			secrets.load(fis);
		} catch (IOException e) {
			throw new ExceptionInInitializerError("[Configuracion] Error leyendo " + SECRETS_FILE + ": " + e.getMessage());
		}
	}

	public static final String BOT_TOKEN       = secrets.getProperty("BOT_TOKEN");
	public static final String BOT_TOKEN_MOVER = secrets.getProperty("BOT_TOKEN_MOVER");

	// Token de sesión de Ninjabet (cabecera X-Session-Token)
	public static final String NINJA_SESSION_TOKEN = secret("NINJA_SESSION_TOKEN");

	// Devuelve el valor de bot.properties sin espacios, o null si no está o está vacío
	private static String secret(String clave) {
		String valor = secrets.getProperty(clave);
		if (valor == null || valor.trim().isEmpty()) {
			return null;
		}
		return valor.trim();
	}
   
   public static final String urlMover="combinazioni=2&action=get_odds_data&uid=" + secrets.getProperty("NINJA_UID") + "&refund=100&"
   		+ "back_stake=100&filterbookies%5B%5D=104&filterbookies%5B%5D=20"
   		+ "&bookies=-0%2C68%2C1%2C54%2C108%2C2%2C75%2C53%2C56%2C59%2C7%2C62%2C61%2C41%2C106%2C39%2C78%2C104%2C102%2C103%2C73%2C40%2C43%2C42%2C76%2C64%2C71%2C44%2C55%2C45%2C107%2C46%2C47%2C29%2C57%2C109%2C48%2C105%2C65%2C20%2C69%2C52%2C74"
   		+ "&rating-from=96&rating-to=&odds-from=2.5&odds-to=&min-liquidity="
   		+ "&sort-column=4&sort-direction=desc&offset=0&date-from=&date-to="
   		+ "&exchange=all&exchanges=all&sport=&betfair-commission=2&matchbook-commission="
   		+ "&bet-type=&rating-type=normal&roll-real-money=100&roll-bonus=100&roll-remaining=100&roll-rating=95&tz=-60";
    
    
   // private static final String[] CHAT_IDS = {"403482161","-1003064907759"};
    /*  ESTE METODO NO SE UTILIZA*/
	public static final String[] CHAT_IDS = {"403482161"};  //<-- este soy yo
  // public static final String[] CHAT_IDS = {"-1003064907759"}; //<-- este es el chat grupal
	
	public static final String[] CHAT_IDS_MOVER = {"403482161"};  //<-- este soy yo
    
    //hola aqui
    //public static final String[] CHAT_IDS_DEBUG = {"403482161"}; //<--- este soy yo
	public static final String[] CHAT_IDS_DEBUG = {"-4914584937"}; //<-- este es el chatDebug
    
    
	public static final String[] CHAT_IDS_VIGILANTE = {"1066152103"}; //<-- este es el chat de lucas
    //public static final String[] CHAT_IDS_VIGILANTE = {"403482161"}; //<-- este soy yo

	
	public static String urlData = "https://api.ninjabet.bet/api/v1/odds/oddsmatcher";
	public static String urlEvents = "https://api.ninjabet.bet/api/v1/odds/search-data";
	public static String urlExchange = "https://ero.betfair.es/www/sports/exchange/readonly/v1/bymarket";
	
	public static Integer FiltroMinutosAntiguedad = 20;
	public static Double restaCuotaCodere = 0.05;

	public static String uid = secrets.getProperty("NINJA_UID");
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

	public static final String CSV_FILE = BASE_DIR + File.separator + "oddsAnteriores.csv";
	public static final String CSV_FILE_HIST = BASE_DIR + File.separator + "oddsAnterioresHist.csv";

	public static final String CSV_FILE_ENTRADAS = BASE_DIR + File.separator + "ENTRADAS" + File.separator;

	public static ArrayList<String> filtroBookies2UP = new ArrayList<String>(Arrays.asList("2", "48", "7", "69", "45","20", "57", "104"));
	public static ArrayList<String> filtroBookies2UP2WAY = new ArrayList<String>(			Arrays.asList("2", "75", "48", "7", "69", "47", "45","20", "57" , "104"));
	public static ArrayList<String> filtroBookiesVacio = new ArrayList<>();
	public static ArrayList<String> filtroApuestas2UP = new ArrayList<String>(Arrays.asList("home", "away"));
	public static ArrayList<String> filtroApuestasHome = new ArrayList<String>(Arrays.asList("home"));
	public static ArrayList<String> filtroApuestasDraw = new ArrayList<String>(Arrays.asList("draw"));
	public static ArrayList<String> filtroApuestasAway = new ArrayList<String>(Arrays.asList("away"));
	
	
	// URL del dutcher de Vilibets (incluye el uid) -> bot.properties: VILIBETS_URL
	public static String urlDataVilibets = secret("VILIBETS_URL");
	public static final String CONF_VILI = BASE_DIR + File.separator + "confVili.txt";
	
	public static ArrayList<String> bookiesVili = new ArrayList<>();
	static {
	    bookiesVili.add("Bet365");
	    bookiesVili.add("Bwin");
	    bookiesVili.add("Kirolbet");
	    //bookiesVili.add("Betfair Exchange");
	    
	}
	
	public static ArrayList<String> ligasVili = new ArrayList<>();
	static {
	    //ligasVili.add("Bundesliga / Alemania");
	    ligasVili.add("Ligue 1 / Francia");
	}

	
	
}
