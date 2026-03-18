// 🔹 Clase Odd
	package dto;
	
    import java.time.LocalDateTime;
import java.util.ArrayList;


public class Odd {
        private String event;
        private String bookie;
        private String rating;
        private String backOdd;
        private String layOdd;
        private String selection;
        private String selectionId;
        private String competition;
        private String update_time;
        private String country;
        private Integer timeInMin;
        
        private String backOddOriginal;
        private String ratingOriginal;
        
        private LocalDateTime fechaPartido;
        private String sFechaPartido;
        
        private String market_id;
        
        ArrayList<Odd> mejoresHome=new ArrayList<Odd>();
        ArrayList<Odd> mejoresDraw=new ArrayList<Odd>();
        ArrayList<Odd> mejoresAway=new ArrayList<Odd>();
        
        Odd exchangeDraw;
        ArrayList<Odd> oddsFusion;
        
        Integer nivelAlerta;
        
        String equipoHome="";
        String equipoAway="";
        
        Long idOdd;
        
        Double stakeEntradaBookie=0.0;
        Double stakeEntradaExchange=0.0;
        Double BackOddEarly=0.0;
        Double stakeEarly=0.0;
        Double porcEarly=0.0;
        Double bookieWins=0.0;
        Double ExchangeWins=0.0;
        
        ArrayList<Odd> cierres=new ArrayList<Odd>();
        
        String tipoOdd="";
                
        private LocalDateTime fechaAlerta;
        
        //formato VILIBET
        private String bookie1;
        private String bookie2;
        private String bookie3;
        private Double odd1;
        private Double odd2;
        private Double odd3;
        private String selection1;
        private String selection2;
        private String selection3;
        
        
        
        
        

        // getters y setters
        public String getEvent() { return event; }
        public void setEvent(String event) { this.event = event; }
        public String getBookie() { return bookie; }
        public void setBookie(String bookie) { this.bookie = bookie; }
        public String getRating() { return rating; }
        public void setRating(String rating) { this.rating = rating; }
        public String getBackOdd() { return backOdd; }
        public void setBackOdd(String backOdd) { this.backOdd = backOdd; }
        public String getLayOdd() { return layOdd; }
        public void setLayOdd(String layOdd) { this.layOdd = layOdd; }
        public String getSelection() { return selection; }
        public void setSelection(String selection) { this.selection = selection; }
        public String getCompetition() { return competition; }
        public void setCompetition(String competition) { this.competition = competition; }
        public String getUpdate_time() { return update_time; }
        public void setUpdate_time(String update_time) { this.update_time = update_time; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
		public Integer getTimeInMin() {
			return timeInMin;
		}
		public void setTimeInMin(Integer timeInMin) {
			this.timeInMin = timeInMin;
		}
		public String getMarket_id() {
			return market_id;
		}
		public void setMarket_id(String market_id) {
			this.market_id = market_id;
		}
		public String getBackOddOriginal() {
			return backOddOriginal;
		}
		public void setBackOddOriginal(String backOddOriginal) {
			this.backOddOriginal = backOddOriginal;
		}
		public String getRatingOriginal() {
			return ratingOriginal;
		}
		public void setRatingOriginal(String ratingOriginal) {
			this.ratingOriginal = ratingOriginal;
		}
		public ArrayList<Odd> getMejoresHome() {
			return mejoresHome;
		}
		public void setMejoresHome(ArrayList<Odd> mejoresHome) {
			this.mejoresHome = mejoresHome;
		}
		public ArrayList<Odd> getMejoresDraw() {
			return mejoresDraw;
		}
		public void setMejoresDraw(ArrayList<Odd> mejoresDraw) {
			this.mejoresDraw = mejoresDraw;
		}
		public ArrayList<Odd> getMejoresAway() {
			return mejoresAway;
		}
		public void setMejoresAway(ArrayList<Odd> mejoresAway) {
			this.mejoresAway = mejoresAway;
		}
		public String getEquipoHome() {
			return equipoHome;
		}
		public void setEquipoHome(String equipoHome) {
			this.equipoHome = equipoHome;
		}
		public String getEquipoAway() {
			return equipoAway;
		}
		public void setEquipoAway(String equipoAway) {
			this.equipoAway = equipoAway;
		}
		
		public String getsFechaPartido() {
			return sFechaPartido;
		}
		public void setsFechaPartido(String sFechaPartido) {
			this.sFechaPartido = sFechaPartido;
		}
		public LocalDateTime getFechaPartido() {
			return fechaPartido;
		}
		public void setFechaPartido(LocalDateTime fechaPartido) {
			this.fechaPartido = fechaPartido;
		}
		public Odd getExchangeDraw() {
			return exchangeDraw;
		}
		public void setExchangeDraw(Odd exchangeDraw) {
			this.exchangeDraw = exchangeDraw;
		}
		public Integer getNivelAlerta() {
			return nivelAlerta;
		}
		public void setNivelAlerta(Integer nivelAlerta) {
			this.nivelAlerta = nivelAlerta;
		}
		public LocalDateTime getFechaAlerta() {
			return fechaAlerta;
		}
		public void setFechaAlerta(LocalDateTime fechaAlerta) {
			this.fechaAlerta = fechaAlerta;
		}
		public ArrayList<Odd> getOddsFusion() {
			return oddsFusion;
		}
		public void setOddsFusion(ArrayList<Odd> oddsFusion) {
			this.oddsFusion = oddsFusion;
		}
		public String getSelectionId() {
			return selectionId;
		}
		public void setSelectionId(String selectionId) {
			this.selectionId = selectionId;
		}
		public Long getIdOdd() {
			return idOdd;
		}
		public void setIdOdd(Long idOdd) {
			this.idOdd = idOdd;
		}
		public Double getStakeEntradaBookie() {
			return stakeEntradaBookie;
		}
		public void setStakeEntradaBookie(Double stakeEntradaBookie) {
			this.stakeEntradaBookie = stakeEntradaBookie;
		}
		public Double getStakeEntradaExchange() {
			return stakeEntradaExchange;
		}
		public void setStakeEntradaExchange(Double stakeEntradaExchange) {
			this.stakeEntradaExchange = stakeEntradaExchange;
		}
		public Double getBackOddEarly() {
			return BackOddEarly;
		}
		public void setBackOddEarly(Double backOddEarly) {
			BackOddEarly = backOddEarly;
		}
		public ArrayList<Odd> getCierres() {
			return cierres;
		}
		public void setCierres(ArrayList<Odd> cierres) {
			this.cierres = cierres;
		}
		public Double getBookieWins() {
			return bookieWins;
		}
		public void setBookieWins(Double bookieWins) {
			this.bookieWins = bookieWins;
		}
		public Double getExchangeWins() {
			return ExchangeWins;
		}
		public void setExchangeWins(Double exchangeWins) {
			ExchangeWins = exchangeWins;
		}
		public Double getPorcEarly() {
			return porcEarly;
		}
		public void setPorcEarly(Double porcEarly) {
			this.porcEarly = porcEarly;
		}
		public Double getStakeEarly() {
			return stakeEarly;
		}
		public void setStakeEarly(Double stakeEarly) {
			this.stakeEarly = stakeEarly;
		}
		public String getTipoOdd() {
			return tipoOdd;
		}
		public void setTipoOdd(String tipoOdd) {
			this.tipoOdd = tipoOdd;
		}
		public String getBookie1() {
			return bookie1;
		}
		public void setBookie1(String bookie1) {
			this.bookie1 = bookie1;
		}
		public String getBookie2() {
			return bookie2;
		}
		public void setBookie2(String bookie2) {
			this.bookie2 = bookie2;
		}
		public String getBookie3() {
			return bookie3;
		}
		public void setBookie3(String bookie3) {
			this.bookie3 = bookie3;
		}
		public Double getOdd1() {
			return odd1;
		}
		public void setOdd1(Double odd1) {
			this.odd1 = odd1;
		}
		public Double getOdd2() {
			return odd2;
		}
		public void setOdd2(Double odd2) {
			this.odd2 = odd2;
		}
		public Double getOdd3() {
			return odd3;
		}
		public void setOdd3(Double odd3) {
			this.odd3 = odd3;
		}
		public String getSelection1() {
			return selection1;
		}
		public void setSelection1(String selection1) {
			this.selection1 = selection1;
		}
		public String getSelection2() {
			return selection2;
		}
		public void setSelection2(String selection2) {
			this.selection2 = selection2;
		}
		public String getSelection3() {
			return selection3;
		}
		public void setSelection3(String selection3) {
			this.selection3 = selection3;
		}
		
				
        
    }