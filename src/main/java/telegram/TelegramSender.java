package telegram;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import conf.Configuracion;
import dto.MenuOpcion;
import dto.Odd;
import utils.AlertasFactory;

public class TelegramSender {


    public static Integer response200_Inicial=0;
    public static Integer response200_Events=0;
    public static Integer response200_Adicional=0;
    public static Integer peticionesAExchange=0;
    public static Integer response403=0;
    public static Integer response400Telegram=0;
    public static Integer eventosIniciales=0;
    public static Integer eventosFinales=0;
    public static Integer alertasEnviadas=0;
    public static Integer conteo=0;
    public static Integer conteoFiltrado=0;
    public static Double ratioMin=100.0;

    // chat grupal: las alertas se envían sin botones
    private static final String CHAT_GRUPAL = "-1003064907759";

    // ============================================================
    // Construcción del JSON para la API de Telegram con Jackson
    // (escapa correctamente comillas, saltos de línea, \, etc.)
    // ============================================================
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static ObjectNode crearPayload(String chatId, String text) {
    	ObjectNode payload = MAPPER.createObjectNode();
    	payload.put("chat_id", chatId);
    	payload.put("text", text);
    	payload.put("parse_mode", "HTML");
    	payload.put("disable_web_page_preview", true);
    	return payload;
    }

    private static ArrayNode addTeclado(ObjectNode payload) {
    	return payload.putObject("reply_markup").putArray("inline_keyboard");
    }

    // añade una fila con un único botón
    private static void addBoton(ArrayNode teclado, String texto, String callback) {
    	ObjectNode boton = teclado.addArray().addObject();
    	boton.put("text", texto);
    	boton.put("callback_data", callback);
    }

    // botones "Entrar <bookie>" de cada odd fusionado
    private static void addBotonesEntrar(ArrayNode teclado, Odd odd) {
    	for (Odd oddFusion : odd.getOddsFusion()) {
    		addBoton(teclado, "Entrar " + AlertasFactory.getNombreBookie(oddFusion.getBookie()), "entrar" + "|" + oddFusion.getIdOdd());
    	}
    }

    // ============================================================
    // Envío único a la API de Telegram (sendMessage)
    // Devuelve el código HTTP, o -1 si ha fallado la conexión.
    // ============================================================
    private static int enviar(String botToken, ObjectNode payload) {
    	try {
    		String json = MAPPER.writeValueAsString(payload);

    		URL url = new URL("https://api.telegram.org/bot" + botToken + "/sendMessage");
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestMethod("POST");
    		conn.setDoOutput(true);
    		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

    		try (OutputStream os = conn.getOutputStream()) {
    			os.write(json.getBytes(StandardCharsets.UTF_8));
    		}

    		int responseCode = conn.getResponseCode();
    		if(responseCode==400) {
    			response400Telegram++;
    		}
    		System.out.println("📩 Telegram response: " + responseCode);

    		// en caso de error Telegram explica el motivo en el cuerpo de la respuesta
    		boolean ok = responseCode >= 200 && responseCode < 300;
    		InputStream is = ok ? conn.getInputStream() : conn.getErrorStream();
    		if (is != null) {
    			try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
    				String line;
    				StringBuilder response = new StringBuilder();
    				while ((line = in.readLine()) != null) {
    					response.append(line);
    				}
    				System.out.println("📩 Respuesta Telegram: " + response);
    			}
    		}
    		if (!ok) {
    			System.out.println("📩 Telegram JSON enviado: " + json);
    		}
    		return responseCode;

    	} catch (Exception e) {
    		e.printStackTrace();
    		return -1;
    	}
    }

    // ============================================================
    // Métodos públicos (misma firma que antes)
    // ============================================================

    public static void sendTelegramMessageAlerta(String text , Odd odd, String chatId) {
    	boolean vili=odd.getTipoOdd().equals("V");
    	boolean ninja=odd.getTipoOdd().equals("N") || odd.getTipoOdd().isEmpty();

    	ObjectNode payload = crearPayload(chatId, text);

    	if(!chatId.equals(CHAT_GRUPAL)) {
    		if(ninja) {
    			ArrayNode teclado = addTeclado(payload);
    			addBoton(teclado, "❌ Quitar este evento de tus alertas", "excluir" + "|" + odd.getIdOdd());
    			addBoton(teclado, "Consultar Opciones 2WAY", "way" + "|" + odd.getIdOdd());
    			addBotonesEntrar(teclado, odd);
    		} else if(vili) {
    			ArrayNode teclado = addTeclado(payload);
    			addBoton(teclado, "❌ Quitar este evento de tus alertas", "excluir" + "|" + odd.getIdOdd());
    		}
    	}

    	enviar(Configuracion.BOT_TOKEN, payload);
    }

    public static void sendTelegramMessageAlertaMover(String text , Odd odd, String chatId) {
    	enviar(Configuracion.BOT_TOKEN_MOVER, crearPayload(chatId, text));
    }

    public static void sendTelegramMessageAlerta2WAY(String text , Odd odd, String chatId) {
    	enviar(Configuracion.BOT_TOKEN, crearPayload(chatId, text));
    }

    public static void sendTelegramMessageDebug(String text) {
    	for (String chatId : Configuracion.CHAT_IDS_DEBUG) {
    		enviar(Configuracion.BOT_TOKEN, crearPayload(chatId, text));
    	}
    }

    public static void sendTelegramMessageConMenuOpciones(String text, String chatId , List<MenuOpcion> opciones) {
    	ObjectNode payload = crearPayload(chatId, text);
    	ArrayNode teclado = addTeclado(payload);
    	for (MenuOpcion menuOpcion : opciones) {
    		addBoton(teclado, menuOpcion.getTexto(), menuOpcion.getCallback());
    	}
    	enviar(Configuracion.BOT_TOKEN, payload);
    }

    public static void sendTelegramMessageVigilante() {
    	StringBuilder mensajeDebug = new StringBuilder();
    	mensajeDebug.append("<b>Debug Ejecucion</b>\n");
    	mensajeDebug.append("Peticiones HTTP403:  <b>").append("1").append("</b>\n");
    	mensajeDebug.append("<b>Probable caída de la VPN. Avisar").append("</b>\n");
    	String text=mensajeDebug.toString();

    	for (String chatId : Configuracion.CHAT_IDS_VIGILANTE) {
    		enviar(Configuracion.BOT_TOKEN, crearPayload(chatId, text));
    	}
    }

}
