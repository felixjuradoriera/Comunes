package dto;

import java.util.List;

public class Root {
	public String updated;
	public int odds_cnt;
    public List<String> dates;
    public List<String> sports;
    public List<String> events;
    public List<String> leagues;
    public List<String> bookies;
    public List<String> b_names;
    public List<String> s_names;
    public List<String> o_names;
    public List<ViliOdd> odds;
	public List<String> getDates() {
		return dates;
	}
	public void setDates(List<String> dates) {
		this.dates = dates;
	}
	public List<String> getSports() {
		return sports;
	}
	public void setSports(List<String> sports) {
		this.sports = sports;
	}
	public List<String> getEvents() {
		return events;
	}
	public void setEvents(List<String> events) {
		this.events = events;
	}
	public List<String> getLeagues() {
		return leagues;
	}
	public void setLeagues(List<String> leagues) {
		this.leagues = leagues;
	}
	public List<String> getBookies() {
		return bookies;
	}
	public void setBookies(List<String> bookies) {
		this.bookies = bookies;
	}
	public List<String> getB_names() {
		return b_names;
	}
	public void setB_names(List<String> b_names) {
		this.b_names = b_names;
	}
	public List<String> getS_names() {
		return s_names;
	}
	public void setS_names(List<String> s_names) {
		this.s_names = s_names;
	}
	public List<ViliOdd> getOdds() {
		return odds;
	}
	public void setOdds(List<ViliOdd> odds) {
		this.odds = odds;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public int getOdds_cnt() {
		return odds_cnt;
	}
	public void setOdds_cnt(int odds_cnt) {
		this.odds_cnt = odds_cnt;
	}
	public List<String> getO_names() {
		return o_names;
	}
	public void setO_names(List<String> o_names) {
		this.o_names = o_names;
	}
    
    
    
    
}
