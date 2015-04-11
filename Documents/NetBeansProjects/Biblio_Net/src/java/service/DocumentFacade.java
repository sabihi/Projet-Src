/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import bean.Document;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Naoufal
 */
@Stateless
public class DocumentFacade extends AbstractFacade<Document> {
    @PersistenceContext(unitName = "Biblio_NetPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentFacade() {
        super(Document.class);
    }
    
    public Integer createCategorie(String tableName,ArrayList<ArrayList<String>> proprieties){
         String nom;
         String type;
         String defaut;
         String requette;
         requette = "CREATE TABLE "+tableName+"(\n";
         int NbrAttribut=proprieties.size()-1;
         int comp=0;
         for (ArrayList<String> arrayList : proprieties) {
            //for (int i = 0; i < arrayList.size(); i++) {
            comp++;
            ArrayList<String> last=Iterables.getLast(proprieties);
            nom=arrayList.get(0);
            type=arrayList.get(1);
            defaut=arrayList.get(2);
            if((proprieties.size())==comp){
                requette+=nom+"  "+ type +" "+ defaut+"\n";
               }
            else
                requette+=nom+"  "+ type +" "+ defaut+",\n";
            //}
        }
        requette+=");";
        Query query=em.createNativeQuery(requette);
        try {
            //System.out.println("---->"+requette);
            query.executeUpdate();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",  "Table a été ajouté avec succès ");  
            FacesContext.getCurrentInstance().addMessage(null, message);
            return 1;
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",  "Une Table portant ce nom existe déjà !");  
            FacesContext.getCurrentInstance().addMessage(null, message);
            return 0;
        }
    }
    
    public List<Object> listerCategories(){
        Query query = em.createNativeQuery("SHOW TABLES LIKE '%_Doc'");
        return query.getResultList();
    }
     public List<Object[]> listerAttributs(String myTable){
        Query query = em.createNativeQuery("SHOW COLUMNS FROM "+myTable);
        List<Object[]> list =query.getResultList();
        return query.getResultList();
    }
     
    public int modifierLigne(String nomCategorie,String nomCol,String nomNvCol,String type,String defaut){
        Query query = em.createNativeQuery("ALTER TABLE "+nomCategorie+" CHANGE "+nomCol+" "+nomNvCol+" "+type+" "+defaut+";");
        System.out.println("--------"+query);
        return query.executeUpdate();
    }
    public int supprimerLigne(String nomCategorie,String nomCol){
        Query query = em.createNativeQuery("ALTER TABLE "+nomCategorie+" DROP COLUMN "+nomCol);
        return query.executeUpdate();
    }
    
    public int ajouterLigne(String nomCategorie,String nomCol,String type,String defaut){
        Query query = em.createNativeQuery("ALTER TABLE "+nomCategorie+" ADD COLUMN "+nomCol+"  "+type+" "+defaut+";");
        return query.executeUpdate();
    }
    public int supprimerCategorie(String nomCategorie){
        Query query = em.createNativeQuery("DROP TABLE "+nomCategorie+";");
        return query.executeUpdate();
    }
}
