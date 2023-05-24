package model.data;

import java.util.Date;

public class Emprunt {

	    private int id;
	    private int tauxApp;
	    private int capital;
	    private int duree;
	    private int idClient;
	    private Date date;


	    public Emprunt(int idEmprunt, int tauxApp, int capital, int duree, Date date, int idClient ) {
	        this.id = idEmprunt;
	        this.tauxApp = tauxApp;
	        this.capital = capital;
	        this.duree = duree;
	        this.date = date;
	        this.idClient = idClient;
	    }

	    public int getId() {
	        return id;
	    }

	    public int getTauxApp() {
	        return tauxApp;
	    }

	    public int getDuree() {
	        return duree;
	    }
	    public int getidclient() {
	        return idClient;
	    }

	    public Date getdate() {
	        return date;
	    }


	    public void setId(int id) {
	        this.id = id;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    @Override
	    public String toString() {
	        return "Animal{" +
	                "id=" + id +
	                ", type='" + type + '\'' +
	                ", name='" + name + '\'' +
	                '}';
	    }
	}
}
