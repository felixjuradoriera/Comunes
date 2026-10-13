package dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Estructura de la respuesta del servicio nuevo de Ninjabet (oddsmatcher).
 *  - evs: lista de cuotas (una por oportunidad back/lay)
 *  - md : datos de cada partido, enlazados con evs por el id de evento (e1)
 *  - tc : total de resultados (para paginar)
 *  - cp : página actual
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NinjaRoot {

	public List<NinjaEv> evs;
	public List<NinjaMd> md;
	public Integer tc;
	public Integer cp;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class NinjaEv {
		public Long id;        // id de la cuota
		public Double rt1;     // rating
		public Long e1;        // id de evento (enlaza con NinjaMd.e1)
		public Integer p1;     // proveedor/casa del back
		public Integer s1;     // selección: 1=local, 2=visitante, 3=empate, otros=descartar
		public Double o1;      // cuota back (casa)
		public Integer p2;     // proveedor del lay (exchange, 10=Betfair)
		public Integer s2;     // selección en el exchange
		public Double o2;      // cuota lay (exchange)
		public Integer sp;     // deporte (1=fútbol)
		public Double lq;      // liquidez
		public String ut;      // fecha/hora de actualización (ISO)
		public Double a1;       // atributo (hándicap/línea) - no usado en 1X2
		public Double a2;
		public String mk;      // market_id (Betfair)
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class NinjaMd {
		public String ev;        // nombre del evento "A v B"
		public String st;        // fecha/hora de inicio (ISO local)
		public String cm;        // competición
		public String ct;        // país
		public List<Long> e1;    // id(s) de evento que enlazan con NinjaEv.e1
	}
}
