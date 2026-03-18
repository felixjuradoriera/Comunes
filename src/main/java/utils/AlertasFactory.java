package utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import dto.Odd;

public class AlertasFactory {
	
	
	
	
	public static StringBuilder createAlerta(Odd odd) {
		
	 	boolean vili=odd.getTipoOdd().equals("V")?true:false;
	 	boolean ninja=odd.getTipoOdd().equals("N")?true:odd.getTipoOdd().isEmpty()?true:false;
		
		StringBuilder mensaje = new StringBuilder();
		
		if(vili) {
			mensaje.append("<b>👉👉👉 V I L I B E T 👈👈👈").append("</b>\n\n");
		}
		if(ninja) {
			mensaje.append("<b>👉👉 N I N J A B E T 👈👈").append("</b>\n\n");
		}
    	
		 if(odd.getNivelAlerta()==1) {
			 mensaje.append("‼️🔥🔥‼️").append("\n");
		 }
		 if(odd.getNivelAlerta()==2) {
			 mensaje.append("‼️‼️🔥🔥🔥🔥🔥🔥‼️‼️").append("\n");
		 }
		 mensaje.append("⚽ <b>").append(odd.getEvent()).append("</b>\n");
		 mensaje.append("🏆 <b>").append(odd.getCompetition()).append("</b>");
		 if(ninja)
		 mensaje.append(" <b>(").append(odd.getCountry()).append(")</b>");
		 
		 mensaje.append("\n");
		 mensaje.append("🗓️ <b>").append(odd.getsFechaPartido()).append("h").append("</b>\n");
		 
		
		 
		 
		 mensaje.append("\n");
		 
		
				 int bookies=odd.getOddsFusion().size();
				 ArrayList oddsFusion=odd.getOddsFusion();
				 oddsFusion.sort(Comparator.comparingDouble(o -> Double.parseDouble(((Odd) o).getRating()))
		                    .reversed());
				 int iteracion=0;
				 for (Odd odFusion : odd.getOddsFusion()) {
					 
					 if(ninja) {
					
						 if(bookies>1) {
							 mensaje.append("‼️🔔<u><b> 2UP SIMPLE </b></u>\n");
						 } else {
							 mensaje.append("🔔<u><b> 2UP SIMPLE </b></u>\n");	 
						 }
						 
						 mensaje.append("    🏛 <b>").append(getNombreBookie(odFusion.getBookie())).append("</b>\n");
						 
						 if (odFusion.getBookie().equals("39") || odFusion.getBookie().equals("20")|| odFusion.getBookie().equals("57")|| odFusion.getBookie().equals("104")) {
							 mensaje.append("    📈 <b>").append(odFusion.getRating()).append("%</b> (").append(odFusion.getRatingOriginal()).append(")\n");
						 } else {
							 mensaje.append("    📈 <b>").append(odFusion.getRating()).append("%</b>\n");
						 }
						             		
						 mensaje.append("    Ap: <b>").append(odFusion.getSelection()).append("</b>\n");
						 
						 if (odFusion.getBookie().equals("39") || odFusion.getBookie().equals("20")|| odFusion.getBookie().equals("57")|| odFusion.getBookie().equals("104")) {
							 mensaje.append("    📋 Back: <b>").append(odFusion.getBackOdd()).append("</b> (").append(odFusion.getBackOddOriginal()).append(") | Lay: <b>").append(odFusion.getLayOdd()).append("</b>\n");	 
						 } else {
							 mensaje.append("    📋 Back: <b>").append(odFusion.getBackOdd()).append("</b> | Lay: <b>").append(odFusion.getLayOdd()).append("</b>\n"); 
					   		 }
					            		 
						mensaje.append("    ⏱ ").append(odFusion.getUpdate_time()).append("\n");
						mensaje.append("    🔗 <a href=\"https://www.betfair.es/exchange/plus/football/market?id=").append(odFusion.getMarket_id()).append("\">Ver en Betfair</a>\n\n"); 
					 }
					 
					 if(vili && iteracion<3) {
						 if(bookies>1) {
							 mensaje.append("‼️🔔<u><b> 3WAY 2UP </b></u>\n");
						 } else {
							 mensaje.append("🔔<u><b> 3WAY 2UP </b></u>\n");	 
						 }
						 mensaje.append("📈 <b>").append(odFusion.getRating()).append("%</b>\n");	 
						 
						 mensaje.append("🟢<b>").append("1-> ").append("</b>");
						 mensaje.append("  ").append(odFusion.getBookie1()).append("->").append(odFusion.getOdd1()).append("\n");
						 mensaje.append("🟢<b>").append("X-> ").append("</b>");
						 mensaje.append("  ").append(odFusion.getBookie2()).append("->").append(odFusion.getOdd2()).append("\n");
						 mensaje.append("🟢<b>").append("2-> ").append("</b>");
						 mensaje.append("  ").append(odFusion.getBookie3()).append("->").append(odFusion.getOdd3()).append("\n");
											 
					 }
					 iteracion++;
				}
				
				if(ninja) {
				 mensaje.append("🔔<u><b> OTRAS BOOKIES </b></u>\n");
				
				if("1".equals(odd.getSelectionId()))
				{
					mensaje.append("🟢<b>").append(odd.getEquipoHome()).append("</b>\n");
					for (Odd o : odd.getMejoresHome()) {
						 if (o.getBookie().equals("39") || o.getBookie().equals("20")|| o.getBookie().equals("57")|| o.getBookie().equals("104")) {
							mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("(").append(o.getBackOddOriginal()).append(")").append("\n");	 
						 } else {
							mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("\n"); 
						 }
					}
				}
			
				if("2".equals(odd.getSelectionId()))
				{
					mensaje.append("🟢<b>").append(odd.getEquipoAway()).append("</b>\n");
					for (Odd o : odd.getMejoresAway()) {
						if (o.getBookie().equals("39") || o.getBookie().equals("20")|| o.getBookie().equals("57")|| o.getBookie().equals("104")) {
							mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("(").append(o.getBackOddOriginal()).append(")").append("\n");	
						} else {
							mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("\n");
						}
					}
				}
		    
				}
		 
		 if(odd.getNivelAlerta()==1) {
			 mensaje.append("‼️🔥🔥‼️").append("\n");
		 }
		 if(odd.getNivelAlerta()==2) {
			 mensaje.append("‼️‼️🔥🔥🔥🔥🔥🔥‼️‼️").append("\n");
		 }
		
		
		
		return mensaje;
		
		
		
	}
	
	public static StringBuilder createAlertaMover(Odd odd) {
		
		StringBuilder mensaje = new StringBuilder();
    	
		mensaje.append("‼️‼️<b>").append("ALERTA MOVER</b>‼️‼️\n");
		 
		mensaje.append("⚽ <b>").append(odd.getEvent()).append("</b>\n");
		 mensaje.append("🏆 <b>").append(odd.getCompetition()).append(" (").append(odd.getCountry()).append(")</b>\n");
		 mensaje.append("🗓️ <b>").append(odd.getsFechaPartido()).append("h").append("</b>\n\n");
		
		 mensaje.append("    🏛 <b>").append(getNombreBookie(odd.getBookie())).append("</b>\n");
		 mensaje.append("    📈 <b>").append(odd.getRating()).append("%</b>\n");
		 mensaje.append("    📋 Back: <b>").append(odd.getBackOdd()).append("</b> | Lay: <b>").append(odd.getLayOdd()).append("</b>\n");
		 
		
		mensaje.append("    ⏱ ").append(odd.getUpdate_time()).append("\n");
		mensaje.append("    🔗 <a href=\"https://www.betfair.es/exchange/plus/football/market?id=").append(odd.getMarket_id()).append("\">Ver en Betfair</a>\n\n"); 
		 
		
		return mensaje;
		
		
	}
	
	public static StringBuilder createFichaEntrada(Odd odd) {
		
		StringBuilder mensaje = new StringBuilder();
		
		 mensaje.append("⚽ <b>").append(odd.getEvent()).append("</b>\n");
		 mensaje.append("🗓️ <b>").append(odd.getsFechaPartido()).append("h").append("</b>\n");
		 mensaje.append("<b>").append("Entrada Realizada:").append("</b>\n");
		 mensaje.append("<b>").append(odd.getSelection()).append("</b> Rat:<b>").append(odd.getRating()).append("%</b>\n");
		 mensaje.append(AlertasFactory.getNombreBookie(odd.getBookie())).append("-><b>").append(odd.getStakeEntradaBookie()).append("€ </b> BACK a <b>").append(odd.getBackOdd()).append("</b>\n");
		 mensaje.append("Exchange->").append(" <b>").append(odd.getStakeEntradaExchange()).append("€ </b> LAY a <b>").append(odd.getLayOdd()).append("</b>\n");
		 
		if(odd.getCierres()!=null && !odd.getCierres().isEmpty()) {
			 mensaje.append("\n");
			mensaje.append("<b>").append("Cierres 2UP:").append("</b>\n");
			for (Odd cierre : odd.getCierres()) {
				mensaje.append("-><b>").append(cierre.getStakeEarly()).append("</b> BACK a <b>").append(cierre.getBackOddEarly()).append("</b>\n");
			}
		}
		 
		 mensaje.append("\n");
		 mensaje.append("<b>").append("Resultado:").append("</b>\n");
		 if(odd.getBookieWins()<0) {
			 mensaje.append("BACK Wins: ").append("🔴 <b>").append(odd.getBookieWins()).append("€ </b> ").append("\n");	 
		 } else {
			 mensaje.append("BACK Wins: ").append("🟢 <b>").append(odd.getBookieWins()).append("€ </b> ").append("\n");
		 }
		 
		 if(odd.getExchangeWins()<0) {
			 mensaje.append("LAY Wins: ").append("🔴 <b>").append(odd.getExchangeWins()).append("€ </b> ").append("\n");	 
		 } else {
			 mensaje.append("LAY Wins: ").append("🟢 <b>").append(odd.getExchangeWins()).append("€ </b> ").append("\n");
		 }
		 
		 
		
		return mensaje;
		
	}
	
	
public static StringBuilder createFichaCierreParcial(Odd odd) {
		
		StringBuilder mensaje = new StringBuilder();
		
		 mensaje.append("⚽ <b>").append(odd.getEvent()).append("</b>\n");
		 mensaje.append("🗓️ <b>").append(odd.getsFechaPartido()).append("h").append("</b>\n");
		 mensaje.append("<b>").append("Entrada Realizada:").append("</b>\n");
		 mensaje.append("<b>").append(odd.getSelection()).append("</b> Rat:<b>").append(odd.getRating()).append("%</b>\n");
		 mensaje.append(AlertasFactory.getNombreBookie(odd.getBookie())).append("-><b>").append(odd.getStakeEntradaBookie()).append("€ </b> BACK a <b>").append(odd.getBackOdd()).append("</b>\n");
		 mensaje.append("Exchange->").append(" <b>").append(odd.getStakeEntradaExchange()).append("€ </b> LAY a <b>").append(odd.getLayOdd()).append("</b>\n");
		 
		 if(odd.getCierres()!=null && !odd.getCierres().isEmpty()) {
			 	mensaje.append("\n");
				mensaje.append("<b>").append("Cierres 2UP:").append("</b>\n");
				for (Odd cierre : odd.getCierres()) {
					mensaje.append("-><b>").append(cierre.getStakeEarly()).append("</b> BACK a <b>").append(cierre.getBackOddEarly()).append("</b>\n");
				}
		}
		 
		 mensaje.append("\n");
		 mensaje.append("👉Apuesta->").append("<b>").append(odd.getStakeEarly()).append("€ </b> BACK a <b>").append(odd.getBackOddEarly()).append("</b>\n\n");
		 		 
		 
		 mensaje.append("<b>").append("Resultado:").append("</b>\n");
		 if(odd.getBookieWins()<0) {
			 mensaje.append("Bookie Wins: ").append("🔴 <b>").append(odd.getBookieWins()).append("€ </b> ").append("\n");	 
		 } else {
			 mensaje.append("Bookie Wins: ").append("🟢 <b>").append(odd.getBookieWins()).append("€ </b> ").append("\n");
		 }
		 
		 if(odd.getExchangeWins()<0) {
			 mensaje.append("Exchange Wins: ").append("🔴 <b>").append(odd.getExchangeWins()).append("€ </b> ").append("\n");	 
		 } else {
			 mensaje.append("Exchange Wins: ").append("🟢 <b>").append(odd.getExchangeWins()).append("€ </b> ").append("\n");
		 }
		 
		 
		
		return mensaje;
		
	}
	
	
	
	public static StringBuilder createAlerta2WAY(Odd odd) {
		
	 	StringBuilder mensaje = new StringBuilder();
    	
		
		 mensaje.append("⚽ <b>").append(odd.getEvent()).append("</b>\n");
		 mensaje.append("🏆 <b>").append(odd.getCompetition()).append(" (").append(odd.getCountry()).append(")</b>\n");
		 mensaje.append("🗓️ <b>").append(odd.getsFechaPartido()).append("h").append("</b>\n\n");
		
		mensaje.append("🔔<u><b> 2WAY 2UP </b></u>\n");
		mensaje.append("🟢<b>").append(odd.getEquipoHome()).append("</b>\n");
		for (Odd o : odd.getMejoresHome()) {
			 if (o.getBookie().equals("39") || o.getBookie().equals("20") || o.getBookie().equals("57")|| o.getBookie().equals("104")) {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("(").append(o.getBackOddOriginal()).append(")").append("\n");	 
			 } else {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("\n"); 
			 }
		}
		
		
		mensaje.append("🟢<b>").append("Empate").append("</b>\n");
		for (Odd o : odd.getMejoresDraw()) {
			if (o.getBookie().equals("39") || o.getBookie().equals("2") || o.getBookie().equals("20")|| o.getBookie().equals("57")|| o.getBookie().equals("104")) {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("(").append(o.getBackOddOriginal()).append(")").append("\n");	
			} else {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("\n");
			}
		}
		
		if(odd.getExchangeDraw()!=null) {
			mensaje.append("     ").append("Exchange BACK ->").append(odd.getExchangeDraw().getBackOdd()).append("(").append(odd.getExchangeDraw().getBackOddOriginal()).append(")\n");
			mensaje.append("       Liquidez:").append(odd.getExchangeDraw().getLayOdd()).append("€\n");
		}
		
		
		mensaje.append("🟢<b>").append(odd.getEquipoAway()).append("</b>\n");
		for (Odd o : odd.getMejoresAway()) {
			if (o.getBookie().equals("39") || o.getBookie().equals("20")|| o.getBookie().equals("57")|| o.getBookie().equals("104")) {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("(").append(o.getBackOddOriginal()).append(")").append("\n");	
			} else {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("\n");
			}
		}
		
			
		return mensaje;
		
		
		
	}
	
	
    public static String getNombreBookie(String bookie) {
        Map<String, String> bookies = new HashMap<>();
        bookies.put("68", "1xbet");
        bookies.put("1", "888sport");
        bookies.put("54", "Admiral");
        bookies.put("108", "Aupabet");
        bookies.put("2", "Bet365");
        bookies.put("75", "Bet777");
        bookies.put("53", "Betfairsportbook");
        bookies.put("56", "Betsson");
        bookies.put("59", "Betway");
        bookies.put("7", "Bwin");
        bookies.put("62", "Casino Barcelona");
        bookies.put("61", "Casino Madrid");
        bookies.put("41", "Casinogranvía");
        bookies.put("106", "Casumo");
        bookies.put("39", "Codere");
        bookies.put("78", "Dafabet");
        bookies.put("104", "Daznbet");
        bookies.put("102", "Ebingo");
        bookies.put("103", "Efbet");
        bookies.put("73", "Enracha");
        bookies.put("40", "Goldenpark");
        bookies.put("43", "Interwetten");
        bookies.put("42", "Jokerbet");
        bookies.put("76", "Juegging");
        bookies.put("64", "Kirolbet");
        bookies.put("71", "Leovegas");
        bookies.put("44", "Luckia");
        bookies.put("55", "Marathonbet");
        bookies.put("45", "MarcaApuestas");
        bookies.put("107", "Olybet");
        bookies.put("46", "Paf");
        bookies.put("47", "Paston");
        bookies.put("29", "Pokerstars");
        bookies.put("57", "Retabet");
        bookies.put("109", "Solcasino");
        bookies.put("48", "Sportium");
        bookies.put("105", "Tonybet");
        bookies.put("65", "Versus");
        bookies.put("20", "Williamhill");
        bookies.put("69", "Winamax");
        bookies.put("52", "Yaass");
        bookies.put("74", "Zebet");

        return bookies.getOrDefault(bookie, bookie);
    }
	
	

}
