package service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import conf.Configuracion;
import dto.Odd;
import dto.Root;
import dto.ViliOdd;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import telegram.TelegramSender;

public class ViliBetsService {
	
	public static Integer codeRespuesta = 0;
	

	 public static ArrayList<Odd> mapearListaResultadosData(List<String> bookies2UP,List<String> bookiesExcluidas,List<String> ligasVili, String urlPeticion, boolean inicial) throws JsonMappingException, JsonProcessingException { 
	    	
		 
		 	String response= crearPeticionData("", Configuracion.urlDataVilibets);
		 	
		 	ArrayList<ViliOdd> odds = new ArrayList<>();
		 	 
	    	
		 	if(!response.isEmpty()) {
		 		ObjectMapper mapper = new ObjectMapper();

	            Root root = mapper.readValue(response, Root.class);
	           
	            for (ViliOdd odd : root.odds) {
	            	
	            	odd.setSport(root.sports.get(odd.s));
	            	odd.setDate(root.dates.get(odd.d));
	            	odd.setEvent(root.events.get(odd.e));
	            	odd.setLeague(root.leagues.get(odd.le));
	            	
	            	if(odd.getB1()>0) {
	            		odd.setBookie1(root.b_names.get(odd.b1));
	            	}
	            	if(odd.getB2()>0) {
	            		odd.setBookie2(root.b_names.get(odd.b2));
	            	}
	            	if(odd.getB3()>0) {
	            		odd.setBookie3(root.b_names.get(odd.b3));
	            	}
	            	
	            	if(odd.getO2o()>0 || odd.getO1o()>0 || odd.getO3o()>0) {
	            		//System.out.println("x");
	            	}
	            	
	            	//filtro ligas/bookies
	            	if(ligasVili.contains(odd.getLeague())) {
	            		if(odd.getBookie1() == null || odd.getBookie1().isEmpty() || bookies2UP.contains(odd.getBookie1())) {
	            			if(odd.getBookie2() == null || odd.getBookie2().isEmpty() || ( !odd.getBookie2().isEmpty() && !bookiesExcluidas.contains(odd.getBookie2()))) {
	            				if(odd.getBookie3() == null || odd.getBookie3().isEmpty() || bookies2UP.contains(odd.getBookie3())) {
	            					
	            					if(odd.getO1o()>0)
	            						odd.setO1(odd.getO1o());
	            					if(odd.getO2o()>0)
	            						odd.setBookie2(odd.getBookie2()+ "(+)");
	            					if(odd.getO3o()>0)
	            						odd.setO3(odd.getO3o());
	            					
	            					//Corrección comision Betfair Exchange
	            					if(odd.getBookie2()!=null  && odd.getBookie2().equals("Betfair Exchange")) {
	            						odd.setBookie2("Bet Exchange"+ "(-)");
	            						odd.setO2(odd.getO2()*0.98);
	            					}
	            						            					
	            					Double prob1=odd.getO1()>0?1/odd.getO1():0;
	            					Double prob2=odd.getO2()>0?1/odd.getO2():0;
	            					Double prob3=odd.getO3()>0?1/odd.getO3():0;
	            					Double prob123=prob1+prob2+prob3;
	            					Double prob=(1/prob123)*100;
	            					BigDecimal probDec=new BigDecimal(prob);
	            					probDec = probDec.setScale(2, RoundingMode.HALF_UP);
	            					double resultadoFinal=probDec.doubleValue();
	            					            					
	            					odd.setRating(resultadoFinal);
	            					if(odd.getRating()<TelegramSender.ratioMin) {
	            						TelegramSender.ratioMin=odd.getRating();
	            					}
	            					            					
	            					if(odd.getBookie1()!=null && odd.getBookie2()!=null && odd.getBookie3()!=null) {
	            						if(odd.getRating()>=97 && odd.getO1()>=1.5 && odd.getO2()>=1.5 && odd.getO3()>=1.5) {
	            						odds.add(odd);	
	            						TelegramSender.conteoFiltrado++;
	            						}
	            					}
	            					
	            				}
	            			}
	            		}
	            		
	            	}
	            	           	
	            	TelegramSender.conteo++;    	
	               }
		 	}
		 	
		 	
		 	ArrayList<Odd> oddsNinja = new ArrayList<>();
		 	
		 	for (ViliOdd odd : odds) {
		 		Odd map=viliToNinja(odd);
		 		oddsNinja.add(map);
			}
		 	
		 return oddsNinja;
	    	
	    }
	 
	 
	
	 public static Odd viliToNinja(ViliOdd viliOdd) {
		 Odd odd=new Odd();
		 odd.setTipoOdd("V");
		 odd.setBookie(viliOdd.getBookie1()+"-"+viliOdd.getBookie2()+"-" + viliOdd.getBookie3());
		 odd.setBookie1(viliOdd.getBookie1());
		 odd.setBookie2(viliOdd.getBookie2());
		 odd.setBookie3(viliOdd.getBookie3());
		 
		 odd.setRating(String.valueOf(viliOdd.getRating()));
		 
		 odd.setBackOdd(""+ viliOdd.getO1()+"-" + viliOdd.getO2()+"-"+ viliOdd.getO3());
		 odd.setOdd1(viliOdd.getO1());
		 odd.setOdd2(viliOdd.getO2());
		 odd.setOdd3(viliOdd.getO3());
		 
		 odd.setCompetition(viliOdd.getLeague());
		 odd.setEvent(viliOdd.getEvent());
		
		 String cadena = viliOdd.getEvent();
		 String[] partes = cadena.split(" vs ");
		 odd.setSelection1(partes[0]);
		 odd.setSelection2("empate");
		 odd.setSelection3(partes[1]);
		 odd.setSelection(odd.getSelection1()+"-"+odd.getSelection2()+"-" + odd.getSelection3());
		 		 
		 OffsetDateTime odt = OffsetDateTime.parse(viliOdd.getDate());
		 LocalDateTime ldtLocal = odt
		         .atZoneSameInstant(ZoneId.of("Europe/Madrid"))
		         .toLocalDateTime();
		 
		 odd.setFechaPartido(ldtLocal);
		 
		 // Definir el formato de salida
         DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
         // Convertir a String en el nuevo formato
         String fechaFormateada = ldtLocal.format(formatterSalida);
         odd.setsFechaPartido(fechaFormateada);
         
		 String data=""+ odd.getCompetition()+odd.getEvent();
		 String market_id="";
		 try {
			market_id=generarToken(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if(!market_id.isEmpty()) {
			 odd.setMarket_id(market_id);
		 }
		 
		 //asignar Nivel Alerta
         Double rating=Double.valueOf(odd.getRating());
         Integer nivelAlerta=0;
         
         	if(rating>95) {
         		nivelAlerta=1;
         	}
         	if(rating>97.5) {
         		nivelAlerta=2;
         	}
                 
         odd.setNivelAlerta(nivelAlerta);
         odd.setTimeInMin(0);
		 		 
		 return odd;
	 }
	 
	 public static String crearPeticionData(String urlParameters, String urlConexion) {
	    	StringBuilder response = new StringBuilder();
	    	
	    	
	    	OkHttpClient client = new OkHttpClient();

	        Request request = new Request.Builder()
	                .url("https://widgets.scrapebet.com/dutcher-full/6324e6ce518a3317260083a2?uid=Rubio")
	                .post(okhttp3.RequestBody.create(new byte[0]))
	                .addHeader("Accept", "application/json")
	                .addHeader("Content-Type", "application/json")
	                .addHeader("Origin", "https://dashboard.vilibets.com")
	                .addHeader("Referer", "https://dashboard.vilibets.com/")
	                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36")
	                .addHeader("Accept-Language", "es-ES,es;q=0.9")
	                .build();
	    	
	        try (Response response1 = client.newCall(request).execute()) {

	            System.out.println("HTTP code VILIBETS: " + response1.code());
	            
	            if (response1.body() != null) {
	            	System.out.println("VILIBETS: TENEMOS BODY");
	                String responseBody = response1.body().string();
	                if(response1.code()>200) {
	                	System.out.println(responseBody);
	                }
	            	
	                return responseBody;
	                
	            }
	        } catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        return "";
	    
	    	
	    }
	 
	  
	    
	    public static String generarToken(String input) throws Exception {
	    	 try {
	             // 1️⃣ Crear hash SHA-256
	             MessageDigest digest = MessageDigest.getInstance("SHA-256");
	             byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

	             // 2️⃣ Nos quedamos con los primeros 15 bytes (~20 chars en Base64)
	             byte[] shortHash = Arrays.copyOf(hash, 15);

	             // 3️⃣ Codificar en Base64 URL-safe sin padding
	             return Base64.getUrlEncoder()
	                     .withoutPadding()
	                     .encodeToString(shortHash);

	         } catch (Exception e) {
	             throw new RuntimeException("Error generando token", e);
	         }
	    }
	    
		
		
}
