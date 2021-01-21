/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.view;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.FloatStringConverter;
import javax.ws.rs.core.GenericType;
import reto2desktopclient.client.EventManager;
import reto2desktopclient.client.EventManagerFactory;
import reto2desktopclient.model.Event;

/**
 * 
 * @author Martin Angulo
 */
public class EventManagementController {

    private static final Logger LOGGER = Logger.getLogger(EventManagementController.class.getName());

    @FXML
    private Stage stage;

    @FXML
    private AdminMenuController adminMenuController;
    
    @FXML
    private TableView tblEvents;
    private ObservableList<Event> eventData;
    @FXML
    private TableColumn<Event, String> colName;
    @FXML
    private TableColumn<Event, Date> colDate;
    @FXML
    private TableColumn<Event, String> colPlace;
    @FXML
    private TableColumn<Event, Float> colPrice;
    @FXML
    private TableColumn<Event, String> colDescription;

    private final EventManager eventManager = EventManagerFactory.getEventManager();
    
    @FXML
    private Label lblError;
    
    private String userLogin;
    
    /**
     * Initializes the scene and its components
     *
     * @param root
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Event Management");
        stage.setResizable(false);
        stage.show();
        adminMenuController.setStage(stage);
        
        //Hide label
        lblError.setVisible(false);
        
        //Make the table editable
        tblEvents.setEditable(true);
        tblEvents.getSelectionModel().cellSelectionEnabledProperty().set(true);
        
        //Set cell editing properties
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPlace.setCellValueFactory(new PropertyValueFactory<>("place"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("ticketprice"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        //Pass Date to String converter
        colDate.setCellFactory(TextFieldTableCell.forTableColumn(new MyDateStringConverter("MM/dd/yy")));       
        colPlace.setCellFactory(TextFieldTableCell.forTableColumn());
        //Pass Float to String converter
        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new MyFloatStringConverter()));
        colDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        
        colName.setOnEditCommit((CellEditEvent<Event, String> t) -> {
            if(validateLength(t.getNewValue())) {
                Event currEvent = t.getRowValue();
                currEvent.setName(t.getNewValue());
                eventManager.edit(currEvent);
                tblEvents.refresh();
            }
        });
        colDate.setOnEditCommit((CellEditEvent<Event, Date> t) -> {
            if(t.getNewValue() == null && 
                !lblError.getText().equals("* Date must have format: mm/dd/yy\nand be possible.")) {
                lblError.setText("* Field must not be empty.");
                lblError.setVisible(true);
            } else {
                Event currEvent = t.getRowValue();
                currEvent.setDate(t.getNewValue());
                eventManager.edit(currEvent);
                tblEvents.refresh();
            }
        });
        colPlace.setOnEditCommit((CellEditEvent<Event, String> t) -> {
            if(validateLength(t.getNewValue())) {
                Event currEvent = t.getRowValue();
                currEvent.setPlace(t.getNewValue());
                eventManager.edit(currEvent);
                tblEvents.refresh();
            }
        });
        colPrice.setOnEditCommit((CellEditEvent<Event, Float> t) -> {
            if(validatePrice(t.getNewValue()) && validateLength(t.getNewValue().toString())) {
                Event currEvent = t.getRowValue();
                currEvent.setTicketprice(t.getNewValue());
                eventManager.edit(currEvent);
                tblEvents.refresh();
            } else {
                Event currEvent = t.getRowValue();
                currEvent.setTicketprice(null);
                eventManager.edit(currEvent);
                tblEvents.refresh();
            }
        });
        colDescription.setOnEditCommit((CellEditEvent<Event, String> t) -> {
            if(validateLength(t.getNewValue())) {
                Event currEvent = t.getRowValue();
                currEvent.setDescription(t.getNewValue());
                eventManager.edit(currEvent);
                tblEvents.refresh();
            }
        });
        
        //Get data and add it to the table
        refreshData();
        
        LOGGER.log(Level.INFO, "Successfully switched to Event Management window.");
    }

    /**
     * Removes an event.
     */
    @FXML
    private void handleButtonRemoveEvent() {
        LOGGER.log(Level.INFO, "Removing event.");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Event?", ButtonType.OK);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Event currEvent = (Event)tblEvents.getFocusModel().getFocusedItem();
            eventManager.remove(currEvent.getId().toString());
            refreshData();
        }
    }

    /**
     * Validates input and adds an event.
     */
    @FXML
    private void handleButtonAddEvent() {
        LOGGER.log(Level.INFO, "Adding event.");
        Event newEvent = new Event();
        eventManager.create(newEvent);
        refreshData();
        tblEvents.getSelectionModel().clearSelection();
        tblEvents.getSelectionModel().select(tblEvents.getItems().size() - 1, colName);
        tblEvents.scrollTo(newEvent);
        tblEvents.edit(tblEvents.getItems().size() - 1, colName);
        tblEvents.refresh();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    private void refreshData() {
        eventData = FXCollections.observableArrayList(eventManager.getAllEvents(new GenericType<List<Event>>(){}));
        tblEvents.setItems(eventData);
    }
    
    private Boolean validateLength(String s) {
        if (s.length() == 0) {
            lblError.setText("* Field must not be empty");
            lblError.setVisible(true);
            return false;
        } else if (s.length() > 255){
            lblError.setText("* Must be less than 255 characters");
            lblError.setVisible(true);
            return false;
        }
        lblError.setVisible(false);
        return true;
    }
    
    private Boolean validatePrice(Float price) {
        if(price == null) {
            lblError.setText("* Field must not be empty.");
            lblError.setVisible(true);
            return false;
        } else if(price < 0) {
            lblError.setText("* Price cannot be negative");
            lblError.setVisible(true);
            return false;
        }
        return true;
    }
    
    public class MyDateStringConverter extends DateStringConverter {

	public MyDateStringConverter(final String pattern) {
		super(pattern);
	}

	@Override
	public Date fromString(String newDate) {
            // Catches the RuntimeException thrown by DateStringConverter.fromString()
            try {
                Date date = super.fromString(newDate);
                lblError.setText("");
                lblError.setVisible(false);
                return date;
            } catch (RuntimeException ex) {
                if(newDate == null) {
                    lblError.setText("* Field must not be empty.");
                    lblError.setVisible(true);
                } else {
                    lblError.setText("* Date must have format: mm/dd/yy\nand be possible.");
                    lblError.setVisible(true);
                }
                return null;
            }
	}
    }
    
    public class MyFloatStringConverter extends FloatStringConverter {

        @Override
	public Float fromString(String newPrice) {
            newPrice = newPrice.replace("€", "");
            return super.fromString(newPrice);
        }
        
	@Override
	public String toString(Float price) {
            return super.toString(price) + "€";
	}
    }
    
    public String getUserLogin() {
        return userLogin;
    }
    
    public void setUserLogin(String login) {
        userLogin = login;
    }
}
