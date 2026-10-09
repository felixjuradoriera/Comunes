package telegram;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
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
    
    /*  ESTE METODO NO SE UTILIZA*/
    public static void sendTelegramMessage(String text) {
    	 for (String chatId : Configuracion.CHAT_IDS) {
        try {
            String urlString = "https://api.telegram.org/bot" + Configuracion.BOT_TOKEN + "/sendMessage";
            String urlParameters = "chat_id=" + chatId
                    + "&text=" + URLEncoder.encode(text, "UTF-8")
                    + "&parse_mode=HTML" // HTML limitado
                    + "&disable_web_page_preview=true";

            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }

            int responseCode = conn.getResponseCode();
            if(responseCode==400) {
            	response400Telegram++;	
            }
            System.out.println("📩 Telegram response: " + responseCode);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("📩 Respuesta Telegram: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    	 }
    }
    
    public static void sendTelegramMessageAlerta(String text , Odd odd, String chatId) {
        
    	boolean vili=odd.getTipoOdd().equals("V")?true:false;
	 	boolean ninja=odd.getTipoOdd().equals("N")?true:odd.getTipoOdd().isEmpty()?true:false;
    	
            try {
                String urlString = "https://api.telegram.org/bot" + Configuracion.BOT_TOKEN + "/sendMessage";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                
                String callBackData="excluir" + "|" + odd.getIdOdd() ;
                String callBackData2WAY="way" + "|" + odd.getIdOdd() ;

                ObjectNode payload = crearPayload(chatId, text);

                // el chat grupal no lleva botones
                if(!chatId.equals("-1003064907759")) {
                	if(ninja) {
                		ArrayNode teclado = addTeclado(payload);
                		addBoton(teclado, "❌ Quitar este evento de tus alertas", callBackData);
                		addBoton(teclado, "Consultar Opciones 2WAY", callBackData2WAY);
                		for (Odd oddFusion : odd.getOddsFusion()) {
                			addBoton(teclado, "Entrar " + AlertasFactory.getNombreBookie(oddFusion.getBookie()), "entrar" + "|" + oddFusion.getIdOdd());
                		}
                	} else if(vili) {
                		ArrayNode teclado = addTeclado(payload);
                		addBoton(teclado, "❌ Quitar este evento de tus alertas", callBackData);
                	}
                }

                String json = MAPPER.writeValueAsString(payload);

                System.out.println("📩 Telegram JSON: " + json);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(json.getBytes(StandardCharsets.UTF_8));
                }

                int responseCode = conn.getResponseCode();
                if(responseCode==400) {
                	response400Telegram++;	
                }
                System.out.println("📩 Telegram response: " + responseCode);

                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("📩 Respuesta Telegram: " + response);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
       
    }
    
    public static void sendTelegramMessageAlertaViliBet(String text , Odd odd, String chatId) {
        
        try {
            String urlString = "https://api.telegram.org/bot" + Configuracion.BOT_TOKEN + "/sendMessage";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            
            String callBackData="excluir" + "|" + odd.getIdOdd() ;
            String callBackData2WAY="way" + "|" + odd.getIdOdd() ;

            ObjectNode payload = crearPayload(chatId, text);

            // el chat grupal no lleva botones
            if(!chatId.equals("-1003064907759")) {
            	ArrayNode teclado = addTeclado(payload);
            	addBoton(teclado, "❌ Quitar este evento de tus alertas", callBackData);
            	addBoton(teclado, "Consultar Opciones 2WAY", callBackData2WAY);
            	for (Odd oddFusion : odd.getOddsFusion()) {
            		addBoton(teclado, "Entrar " + AlertasFactory.getNombreBookie(oddFusion.getBookie()), "entrar" + "|" + oddFusion.getIdOdd());
            	}
            }

            String json = MAPPER.writeValueAsString(payload);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if(responseCode==400) {
            	response400Telegram++;	
            }
            System.out.println("📩 Telegram response: " + responseCode);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("📩 Respuesta Telegram: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
   
}
    
    
    public static void sendTelegramMessageAlertaMover(String text , Odd odd, String chatId) {
        
        try {
            String urlString = "https://api.telegram.org/bot" + Configuracion.BOT_TOKEN_MOVER + "/sendMessage";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
         
            
           String json = MAPPER.writeValueAsString(crearPayload(chatId, text));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if(responseCode==400) {
            	response400Telegram++;	
            }
            System.out.println("📩 Telegram response: " + responseCode);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("📩 Respuesta Telegram: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
   
}
    
    public static void sendTelegramMessageAlerta2WAY(String text , Odd odd, String chatId) {
        
        try {
            String urlString = "https://api.telegram.org/bot" + Configuracion.BOT_TOKEN + "/sendMessage";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            
            String json = MAPPER.writeValueAsString(crearPayload(chatId, text));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if(responseCode==400) {
            	response400Telegram++;	
            }
            System.out.println("📩 Telegram response: " + responseCode);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("📩 Respuesta Telegram: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
   
}

    
    
    public static void sendTelegramMessageDebug(String text) {
   	 for (String chatId : Configuracion.CHAT_IDS_DEBUG) {
       try {
           String urlString = "https://api.telegram.org/bot" + Configuracion.BOT_TOKEN + "/sendMessage";
           String urlParameters = "chat_id=" + chatId
                   + "&text=" + URLEncoder.encode(text, "UTF-8")
                   + "&parse_mode=HTML"; // HTML limitado

           byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

           URL url = new URL(urlString);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("POST");
           conn.setDoOutput(true);

           try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
               wr.write(postData);
           }

           int responseCode = conn.getResponseCode();
           if(responseCode==400) {
           	response400Telegram++;	
           }
           System.out.println("📩 Telegram response: " + responseCode);

           try (BufferedReader in = new BufferedReader(
                   new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
               String line;
               StringBuilder response = new StringBuilder();
               while ((line = in.readLine()) != null) {
                   response.append(line);
               }
               System.out.println("📩 Respuesta Telegram: " + response);
           }

       } catch (Exception e) {
           e.printStackTrace();
       }
   	 }
   }
    
    
    public static void sendTelegramMessageConMenuOpciones(String text, String chatId , List<MenuOpcion> opciones) {
        
        try {
            String urlString = "https://api.telegram.org/bot" + Configuracion.BOT_TOKEN + "/sendMessage";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            
        
			ObjectNode payload = crearPayload(chatId, text);
			ArrayNode teclado = addTeclado(payload);
			for (MenuOpcion menuOpcion : opciones) {
				addBoton(teclado, menuOpcion.getTexto(), menuOpcion.getCallback());
			}
			String json = MAPPER.writeValueAsString(payload);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if(responseCode==400) {
            	response400Telegram++;	
            }
            System.out.println("📩 Telegram response: " + responseCode);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("📩 Respuesta Telegram: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
   
}
    
    
    
    public static void sendTelegramMessageVigilante() {
    	
    	 StringBuilder mensajeDebug = new StringBuilder();
         mensajeDebug.append("<b>Debug Ejecucion</b>\n");
         mensajeDebug.append("Peticiones HTTP403:  <b>").append("1").append("</b>\n");
         mensajeDebug.append("<b>Probable caída de la VPN. Avisar").append("</b>\n");
         String text=mensajeDebug.toString();
         
      	 for (String chatId : Configuracion.CHAT_IDS_VIGILANTE) {
          try {
              String urlString = "https://api.telegram.org/bot" + Configuracion.BOT_TOKEN + "/sendMessage";
              String urlParameters = "chat_id=" + chatId
                      + "&text=" + URLEncoder.encode(text, "UTF-8")
                      + "&parse_mode=HTML"; // HTML limitado

              byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

              URL url = new URL(urlString);
              HttpURLConnection conn = (HttpURLConnection) url.openConnection();
              conn.setRequestMethod("POST");
              conn.setDoOutput(true);

              try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                  wr.write(postData);
              }

              int responseCode = conn.getResponseCode();
              if(responseCode==400) {
              	response400Telegram++;	
              }
              System.out.println("📩 Telegram response: " + responseCode);

              try (BufferedReader in = new BufferedReader(
                      new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                  String line;
                  StringBuilder response = new StringBuilder();
                  while ((line = in.readLine()) != null) {
                      response.append(line);
                  }
                  System.out.println("📩 Respuesta Telegram: " + response);
              }

          } catch (Exception e) {
              e.printStackTrace();
          }
      	 }
      }
    
}
