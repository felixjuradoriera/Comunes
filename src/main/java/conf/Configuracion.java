package conf;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Configuracion {
	
	 // ⚠️ Sustituye por los tuyos
	public static final String BOT_TOKEN = "7380837153:AAHMQFIyGwO-FSwq9DvpQjnH4JroSy9tOSs";  //PRO
  // public static final String BOT_TOKEN = "7029538813:AAH2I40DoMKEWLpVph3qrWUJ3vilGTEQABg";  //PRE
   public static final String BOT_TOKEN_MOVER = "7029538813:AAH2I40DoMKEWLpVph3qrWUJ3vilGTEQABg";  //PRE
   
   public static final String urlMover="combinazioni=2&action=get_odds_data&uid=e7dfca01f394755c11f853602cb2608a&refund=100&"
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
	public static final String CSV_FILE_HIST = "C:" + File.separator + "BOT" + File.separator + "CONF" + File.separator + "oddsAnterioresHist.csv";
	
	public static final String CSV_FILE_ENTRADAS = "C:" + File.separator + "BOT" + File.separator + "CONF" + File.separator + "ENTRADAS" + File.separator;

	public static ArrayList<String> filtroBookies2UP = new ArrayList<String>(Arrays.asList("2", "48", "7", "39", "69", "45","20"));
	public static ArrayList<String> filtroBookies2UP2WAY = new ArrayList<String>(			Arrays.asList("2", "75", "48", "7", "39", "69", "47", "45","20"));
	public static ArrayList<String> filtroBookiesVacio = new ArrayList<>();
	public static ArrayList<String> filtroApuestas2UP = new ArrayList<String>(Arrays.asList("home", "away"));
	public static ArrayList<String> filtroApuestasHome = new ArrayList<String>(Arrays.asList("home"));
	public static ArrayList<String> filtroApuestasDraw = new ArrayList<String>(Arrays.asList("draw"));
	public static ArrayList<String> filtroApuestasAway = new ArrayList<String>(Arrays.asList("away"));

	
	
}
