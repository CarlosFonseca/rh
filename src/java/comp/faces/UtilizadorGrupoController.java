package comp.faces;

import comp.entities.UtilizadorGrupo;
import comp.entities.Utilizadores;
import comp.faces.util.JsfUtil;
import comp.session.UtilizadorGrupoFacade;

import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import org.primefaces.model.LazyDataModel;
import java.util.List;
import java.util.Map;
import org.primefaces.model.SortOrder;


@ManagedBean (name="utilizadorGrupoController")
@SessionScoped
public class UtilizadorGrupoController {

    private UtilizadorGrupo current;
    private LazyDataModel items = null;
    @EJB 
    private comp.session.UtilizadorGrupoFacade ejbFacade;
    private int selectedItemIndex;
    private int tableWidth;

    public UtilizadorGrupoController() {
    }

    public String utilizadores(Object o){      
        current.setUtilizadores((Utilizadores) o);      
        return null; 
    }
    
    public UtilizadorGrupo getSelected() {
        if (current == null) {
            current = new UtilizadorGrupo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UtilizadorGrupoFacade getFacade() {
        return ejbFacade;
    }

    public int getTableWidth(){        
        tableWidth = ((getSelected().getClass().getDeclaredFields().length -6)*115);
        return tableWidth ;
    }

    public void setTableWidth(int tableWidth) {
        this.tableWidth = tableWidth;
    }

    private void beforeSave() throws Exception  {
        getSelected().setLogNre(JsfUtil.getUserPrincipal());
        getSelected().setDtLog(new java.util.Date());                 
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (UtilizadorGrupo)getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new UtilizadorGrupo();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            beforeSave();
            getFacade().create(current);
			recreateModel();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CreateSuccessMessage"));
            return prepareCreate();
        } catch (Exception e) {
	    //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.addErrorMessage(e, e.getMessage());
            return null;
        }
    }

    public String prepareEdit() {
        current = (UtilizadorGrupo)getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            beforeSave();
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UpdateSuccessMessage"));
            return "View";
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            JsfUtil.addErrorMessage(e, e.getMessage());
            return null;
        }
    }

    public void prepareDestroy() {
        current = (UtilizadorGrupo)getItems().getRowData();
        selectedItemIndex =  getItems().getRowIndex();
    }

    public String destroy() {
        performDestroy();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DeleteSuccessMessage"));
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
	    JsfUtil.addErrorMessage(e, e.getMessage());
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count-1;
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex+1}).get(0);
        }
    }

    public LazyDataModel getItems() {
        if (items == null) {
            items = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad  =  getFacade().findRange(new int[]{fist,fist+pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            items.setPageSize(10);
            items.setRowCount(getFacade().count());
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass=UtilizadorGrupo.class)
    public static class UtilizadorGrupoControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UtilizadorGrupoController controller = (UtilizadorGrupoController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "utilizadorGrupoController");
            return controller.ejbFacade.find(getKey(value));
        }

        comp.entities.UtilizadorGrupoPK getKey(String value) {
            comp.entities.UtilizadorGrupoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new comp.entities.UtilizadorGrupoPK();
            key.setNre(values[0]);
            key.setGrupoId(values[1]);
            return key;
        }

        String getStringKey(comp.entities.UtilizadorGrupoPK value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.getNre());
            sb.append(SEPARATOR);
            sb.append(value.getGrupoId());
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof UtilizadorGrupo) {
                UtilizadorGrupo o = (UtilizadorGrupo) object;
                return getStringKey(o.getUtilizadorGrupoPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+UtilizadorGrupoController.class.getName());
            }
        }

    }

}