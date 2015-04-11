/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;

/**
 *
 * @author Naoufal
 */
@Entity
public class Ouvrage extends Document implements Serializable {
    private static final long serialVersionUID = 1L;


    private String titreOuvrage;
    private String volume;
    private String nbrVolume;
    private int pages;
    private String lieu;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    private int doi;
    private int issn;
    private String url;
    private String edition;

    
    
  
    public String getTitreOuvrage() {
        return titreOuvrage;
    }

    public void setTitreOuvrage(String titreOuvrage) {
        this.titreOuvrage = titreOuvrage;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getNbrVolume() {
        return nbrVolume;
    }

    public void setNbrVolume(String nbrVolume) {
        this.nbrVolume = nbrVolume;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDoi() {
        return doi;
    }

    public void setDoi(int doi) {
        this.doi = doi;
    }

    public int getIssn() {
        return issn;
    }

    public void setIssn(int issn) {
        this.issn = issn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

   
    
    
    
    
    
    
    
    

    @Override
    public String toString() {
        return "bean.Ouvrage[ id=" + id + " ]";
    }
    
}
