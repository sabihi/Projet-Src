package controller;

import bean.Document;
import controller.util.JsfUtil;
import controller.util.PaginationHelper;
import service.DocumentFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("documentNController")
@SessionScoped
public class DocumentNController implements Serializable {

    private Document current;
    private DataModel items = null;
    @EJB
    private service.DocumentFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private ArrayList<ArrayList<String>> enregistrements = new ArrayList<ArrayList<String>>();
    private ArrayList<String> enregistrement = new ArrayList<String>();
    private String nomCategorie;//Nom de la table
    private String nomAttribit;//Nom de l'attribut
    private String type;//le type String ,Number 
    private String defaut;//Null or not null
    private String categorieSelected;//La catégorie dont on veut savoir la liste de ses attributs
    

    public void ajouterLigne(AjaxBehaviorEvent e){
        ArrayList<String> enregistrement = new ArrayList<String>();
        if(estPresent()){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",  "Un attribut portant ce nom existe déjà !");  
            FacesContext.getCurrentInstance().addMessage(null, message);
            nomAttribit=null;
        }
        else {
                enregistrement.add(nomAttribit);
                enregistrement.add(type);
                enregistrement.add(defaut);
                enregistrements.add(enregistrement);
        }
    }
  
    public boolean estPresent(){
        for (ArrayList enreg : enregistrements) {
            if(enreg.get(0).equals(nomAttribit))
                return true;
        }
        return false;
    }
    public void afficher(ArrayList<ArrayList<String>> enregistrements){
        for (int i = 0; i < enregistrements.size(); i++) {
            ArrayList<String> enreg = enregistrements.get(i);
           // for (int j = 0; j < enreg.size(); j++) {
                System.out.println("--->"+enreg.get(0));
            //}
        }
    }
    public String supprimerLigne(ArrayList<String> myObject){
        if(myObject.get(0).equals("ID")){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",  "Impossible de supprimer cet Attribut");  
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        else
            enregistrements.remove(myObject);
        return "Create";
//        for (ArrayList enreg : enregistrements) {
//            for (int i = 0; i < enreg.size(); i++) {
//                if(enreg.equals(myObject))
//                    enregistrements.remove(enreg);
//            }
//        }
    }
    //@PostConstruct
    public void init(){
        nomCategorie="";
        if(enregistrements.isEmpty()){
            enregistrement.add("ID");
            enregistrement.add("INT");
            enregistrement.add("NOT NULL");
            enregistrements.add(0, enregistrement);
        }
    }
    
    public String creerCategorie(AjaxBehaviorEvent e){
        if(nomCategorie == null || nomCategorie.isEmpty()){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "",  "Veuillez entrer le nom de la Table");  
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "";
        }
        else {
            if(enregistrements.size()<=1){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",  "Veuillez ajouter un autre attribut");  
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }
            else{
                this.nomCategorie=nomCategorie+"_Doc";
                ejbFacade.createCategorie(nomCategorie,enregistrements);
                enregistrements.clear();//Vider la liste des attributs
                nomCategorie="";
                nomAttribit="";
                init();//Initilialiser la liste 
                return "List";
            }
        }
    }
    public int chercherElemnet(ArrayList<String> enregistrement){
        ArrayList<String> myArrayList=new ArrayList<>();
        for (int i = 0; i < enregistrements.size(); i++) {
            myArrayList=enregistrements.get(i);
            for (int j = 0; j < myArrayList.size(); j++) {
                if(myArrayList.equals(enregistrement))
                    return i;
            }
        }
        return -1;//Enregistrement Introuvable
    }
    
    private ArrayList<String> auxList = new ArrayList<String>();
    public void editEvent(ArrayList<String> myArrayList){
        this.nomAttribit=myArrayList.get(0);
        this.type=myArrayList.get(1);
        this.defaut=myArrayList.get(2);
        this.auxList.clear();
        remplirObjet();
        this.enregistrement.clear();
    }
    
    public void remplirObjet(){
         this.auxList.add(nomAttribit);
         this.auxList.add(type);
         this.auxList.add(defaut);
    }
    public void edition(AjaxBehaviorEvent event){    
        ArrayList<String> enreg = new ArrayList<String>();
        int indice=enregistrements.indexOf(auxList);
        if(indice!=-1){
            //afficher(enregistrements);
            enregistrements.remove(indice);
            enreg.add(nomAttribit);
            enreg.add(type);
            enreg.add(defaut);
            enregistrements.add(indice,enreg);
        }
    }
    
    //2ieme partie:Modification+supprission
    List<Object> strings= new ArrayList<>();
    private Object[] aux;
    private List<Object[]> listeAttributs =new ArrayList<Object[]>();
    private Object[] ligne;
    
    //Lister toutes les catégories de la bd
    public List<Object> listerCategories(){
        return ejbFacade.listerCategories();
    }
    public String supprimerCategorie(){
        ejbFacade.supprimerCategorie(nomCategorie);
        return "List";
    }

    //Lister les données de chaque table aparir de la bd (Nom,type,valeur par defaut)
    public void listerAttributs(AjaxBehaviorEvent e){
        listeAttributs= ejbFacade.listerAttributs(nomCategorie);
    }
    public void initComboBox(AjaxBehaviorEvent e){
        nomCategorie= (String) ejbFacade.listerCategories().get(0);
        listeAttributs= ejbFacade.listerAttributs(nomCategorie);
        
    }
     public void loadList(AjaxBehaviorEvent e){
        nomCategorie= (String) ejbFacade.listerCategories().get(0);
        if(nomCategorie!=null){
            listeAttributs= ejbFacade.listerAttributs(nomCategorie);
        }
    }
     public void loadCreate(AjaxBehaviorEvent e){
            nomCategorie= "";
    }
    String NomCol;
    /*Pour editer les propriétés des champs dans la base de données */
    public void editLigneEvent(Object[] myArrayList){
        ligne=myArrayList;//la ligne à modifier
        aux=myArrayList;//la ligne à supprimer
        NomCol =(String) aux[0];
        
    }
    public String editionLigne(AjaxBehaviorEvent event){
        int indice=listeAttributs.indexOf(aux);
        changeVar();
        modifierLigne(nomCategorie, NomCol,(String) ligne[0], (String) ligne[1], (String) ligne[2]);
        listeAttributs.remove(aux);
        listeAttributs.add(indice, ligne);
        return "List";
    }

    public int modifierLigne(String nomCategorie,String nomCol,String nomNvCol,String type,String defaut){
        return ejbFacade.modifierLigne(nomCategorie, nomCol, nomNvCol, type,defaut);
//        if(operation==1){
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",  "Données a été mise a jour");  
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
//        else{
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "",  "Erreur");  
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }   
 //       return "List";
    }
    public String supprimerLigne(Object[] myArrayList){
        if(myArrayList[0].equals("ID")){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "",  "Impossible de suprimmer l'attribut portant ce nom '"+myArrayList[0]+"'");  
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        else {
            try {
                ejbFacade.supprimerLigne(nomCategorie, (String)myArrayList[0]);
                listeAttributs.remove(myArrayList);
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "",  "Impossible de suprimmer l'attribut portant ce nom '"+myArrayList[0]+"'");  
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        }
        return "List";
        
    }
    
    public boolean estPresent(List<Object[]> myObjects){
        for (Object[] enreg : myObjects) {
            if(enreg[0].equals(nomAttribit))
                return true;
        }
        return false;
    }
    
    public void ajouterLigne(){
        if(!estPresent(listeAttributs)){
            //listeAttributs.add(ligne);
            ejbFacade.ajouterLigne(nomCategorie, nomAttribit, type, defaut);
            System.out.println("----------------------"+nomCategorie+"----"+nomAttribit);
        }
        else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",  "Un attribut portant ce nom existe déjà !");  
            FacesContext.getCurrentInstance().addMessage(null, message);
            System.out.println("----------------------"+nomCategorie+"----"+nomAttribit);
            nomAttribit=null;
        }
    }

    
    public Object[] getLigne() {
        return ligne;
    }
    
    public void changeVar(){
        for (Object object : ligne) {
            if(ligne[2].equals("NO")){
                ligne[2]="NOT NULL";
            }
            else{
                assert ligne[2].equals("YES") : ligne[2]="DEFAULT NULL";
            }
        }
    }
    

    public void setLigne(Object[] ligne) {
        this.ligne = ligne;
    }

    public List<Object[]> getListeAttributs() {
        return listeAttributs;
    }

    public void setListeAttributs(List<Object[]> listAttributs) {
        this.listeAttributs = listAttributs;
    }

    
    public String getCategorieSelected() {
        return categorieSelected;
    }

    public void setCategorieSelected(String categorieSelected) {
        this.categorieSelected = categorieSelected;
    }

    
    public List<Object> getStrings() {
        return strings;
    }

    public void setStrings(List<Object> strings) {
        this.strings = strings;
    }
    
    public ArrayList<ArrayList<String>> getEnregistrements() {
        return enregistrements;
    }

    public void setEnregistrements(ArrayList<ArrayList<String>> enregistrements) {
        this.enregistrements = enregistrements;
    }

    public String getNomAttribit() {
        return nomAttribit;
    }

    public void setNomAttribit(String nomAttribit) {
        this.nomAttribit = nomAttribit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaut() {
        return defaut;
    }

    public void setDefaut(String defaut) {
        this.defaut = defaut;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }
    
    
    

    public DocumentNController() {
        init();
    }

    public Document getSelected() {
        if (current == null) {
            current = new Document();
            selectedItemIndex = -1;
        }
        return current;
    }

    private DocumentFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Document) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Document();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DocumentCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Document) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DocumentUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Document) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DocumentDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Document getDocument(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Document.class)
    public static class DocumentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DocumentNController controller = (DocumentNController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "documentController");
            return controller.getDocument(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Document) {
                Document o = (Document) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Document.class.getName());
            }
        }

    }

}
