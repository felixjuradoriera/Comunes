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

import conf.Configuracion;
import dto.MenuOpcion;
import dto.Odd;

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
        
            try {
                String urlString = "https://api.telegram.org/bot" + Configuracion.BOT_TOKEN + "/sendMessage";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                
                String callBackData="excluir" + "|" + odd.getMarket_id() + "|" + odd.getsFechaPartido() ;
                String callBackData2WAY="way" + "|" + odd.getMarket_id() + "|" + odd.getEvent();

               String json="";
                if(chatId.equals("-1003064907759")) {
                	json = "{"
                            + "\"chat_id\":\"" + chatId + "\","
                            + "\"text\":\"" + text.replace("\"", "\\\"") + "\","
                            + "\"parse_mode\":\"HTML\","
                            + "\"disable_web_page_preview\":true"
                            + "}";
                	
                } else {
                	 json = "{"
                             + "\"chat_id\":\"" + chatId + "\","
                             + "\"text\":\"" + text.replace("\"", "\\\"") + "\","
                             + "\"parse_mode\":\"HTML\","
                             + "\"disable_web_page_preview\":true,"
                             + "\"reply_markup\":{"
                             + "   \"inline_keyboard\":["
                             + "       [{\"text\":\"❌ Quitar este evento de tus alertas\",\"callback_data\":\""+ callBackData +"\"}],"
                             + "       [{\"text\":\"Consultar Opciones 2WAY\",\"callback_data\":\""+ callBackData2WAY +"\"}]"
                             + "   ]"
                             + "}"
                             + "}";
                }
                
               

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
            
            String callBackData="excluir" + "|" + odd.getMarket_id() + "|" + odd.getsFechaPartido();
            String callBackData2WAY="way" + "|" + odd.getMarket_id() + "|" + odd.getEvent();

           String json="";
           
            	json = "{"
                        + "\"chat_id\":\"" + chatId + "\","
                        + "\"text\":\"" + text.replace("\"", "\\\"") + "\","
                        + "\"parse_mode\":\"HTML\","
                        + "\"disable_web_page_preview\":true"
                        + "}";
            	
           

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
            
        
			String json = "{";
			json += "\"chat_id\":\"" + chatId + "\",";
			json += "\"text\":\"" + text.replace("\"", "\\\"") + "\",";
			json += "\"parse_mode\":\"HTML\",";
			json += "\"disable_web_page_preview\":true,";
			json += "\"reply_markup\":{";
			json += "   \"inline_keyboard\":[";
			
			for (MenuOpcion menuOpcion : opciones) {
				json += "       [{\"text\":\""+ menuOpcion.getTexto() +"\",\"callback_data\":\"" + menuOpcion.getCallback() + "\"}],";
			}
			json = json.substring(0, json.length() - 1);
			json += "   ]";
			json += "}";
			json += "}";
           
            
           

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
