package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import conf.Configuracion;
import dto.Odd;

public class OddsCSVUtils {
	
	
	
	 // 🔹 Guardar odds en CSV
    public static void escribirCSV(String file, ArrayList<Odd> odds) {
    	
    	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    	 
    	
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (Odd o : odds) {
            	String fechaFormateada = o.getFechaAlerta().format(formatter);
                pw.println(String.join(";",
                        o.getEvent(), o.getBookie(), o.getRating(), o.getBackOdd(),
                        o.getLayOdd(), o.getSelection(), o.getCompetition(),
                        o.getUpdate_time(), o.getCountry(), o.getTimeInMin().toString(), fechaFormateada, String.valueOf(o.getIdOdd()), o.getsFechaPartido(), o.getMarket_id()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Leer odds desde CSV
    public static ArrayList<Odd> leerCSV(String file) {
        ArrayList<Odd> lista = new ArrayList<>();
        File f = new File(file);
        if (!f.exists()) return lista;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
            	Odd o = new Odd();
                String[] campos = line.split(";");
               
                if (campos.length >= 10) {
                   
                    o.setEvent(campos[0]);
                    o.setBookie(campos[1]);
                    o.setRating(campos[2]);
                    o.setBackOdd(campos[3]);
                    o.setLayOdd(campos[4]);
                    o.setSelection(campos[5]);
                    o.setCompetition(campos[6]);
                    o.setUpdate_time(campos[7]);
                    o.setCountry(campos[8]);
                    o.setTimeInMin(Integer.valueOf(campos[9]));
                    LocalDateTime fecha = LocalDateTime.parse(campos[10], formatter);
                    o.setFechaAlerta(fecha);
                   
                }
                if (campos.length >= 11) {
                	o.setIdOdd(Long.valueOf(campos[11]));
                }
                if (campos.length >= 12) {
                	o.setsFechaPartido(campos[12]);
                	DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                	
                	LocalDateTime fecha=LocalDateTime.parse(o.getsFechaPartido(), formatterSalida);
                	o.setFechaPartido(fecha);
                	              	
                	
                	
                }
                if (campos.length >= 13) {
                	o.setMarket_id(campos[13]);
                	               	
                }
                
                lista.add(o);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    
    public static Odd recuperarOdd(String sIdOdd) {
    	
    	if(sIdOdd!=null && !sIdOdd.isEmpty()) {
    		Long idOdd=Long.valueOf(sIdOdd);	
    		ArrayList<Odd> oddsAnterioresHist = OddsCSVUtils.leerCSV(Configuracion.CSV_FILE_HIST);
    		for (Odd odd : oddsAnterioresHist) {
				if(odd.getIdOdd().longValue()==idOdd.longValue()) {
					return odd;
				}
			}
    		
    		return null;
    	} else {
    		return null;
    	}
    	    	
    	
    }
	
	

}
