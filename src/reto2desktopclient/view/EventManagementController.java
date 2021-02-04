/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.FloatStringConverter;
import javax.ws.rs.core.GenericType;
import reto2desktopclient.client.ClubManagerFactory;
import reto2desktopclient.client.EventManager;
import reto2desktopclient.client.EventManagerFactory;
import reto2desktopclient.client.UserManagerFactory;
import reto2desktopclient.model.Artist;
import reto2desktopclient.model.Client;
import reto2desktopclient.model.Club;
import reto2desktopclient.model.Event;
import reto2desktopclient.model.User;

/**
 * TODO: Check price and date conditions for lblErrors
 * Don't save long strings.
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
    @FXML
    private TableColumn<Event, String> colClub;
    
    private final EventManager eventManager = EventManagerFactory.getEventManager();
    
    @FXML
    private Label lblError;
    
    @FXML
    private Button btnAddEvent;
    
    @FXML
    private Button btnRemoveEvent;
    
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
        
        //Focus add event button
        btnAddEvent.requestFocus();
        
        //Make the table editable
        tblEvents.setEditable(true);
        //tblEvents.getSelectionModel().cellSelectionEnabledProperty().set(true);
        
        //Set cell editing properties
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPlace.setCellValueFactory(new PropertyValueFactory<>("place"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("ticketprice"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colClub.setCellValueFactory(new PropertyValueFactory<>("club"));
        
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        //Pass Date to String converter
        colDate.setCellFactory(TextFieldTableCell.forTableColumn(new MyDateStringConverter("dd/MM/yy")));       
        colPlace.setCellFactory(TextFieldTableCell.forTableColumn());
        //Pass Float to String converter
        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new MyFloatStringConverter()));
        colDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        
        List<Club> clubs = ClubManagerFactory.getClubManager().getAllClubs(new GenericType<List<Club>>(){});
        List<String> clubNames = new ArrayList<>();
        for(Club c : clubs) {
            clubNames.add(c.getLogin());
        }
        colClub.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(clubNames)));
        
        colName.setOnEditCommit((CellEditEvent<Event, String> t) -> {
            if(validateLength(t.getNewValue())) {
                Event currEvent = t.getRowValue();
                currEvent.setName(t.getNewValue());
                eventManager.edit(currEvent);
                tblEvents.refresh();
            } else {
                Event currEvent = t.getRowValue();
                currEvent.setName(t.getOldValue());
                tblEvents.refresh();
            }
        });
        colDate.setOnEditCommit((CellEditEvent<Event, Date> t) -> {
            if(t.getNewValue() != null) {
                Event currEvent = t.getRowValue();
                currEvent.setDate(t.getNewValue());
                eventManager.edit(currEvent);
                tblEvents.refresh();
            } else if(t.getNewValue() == null && 
                !lblError.getText().equals("* Date must have format: dd/mm/yy\nand be possible.")) {
                lblError.setText("* Field must not be empty.");
                lblError.setVisible(true);
                Event currEvent = t.getRowValue();
                currEvent.setDate(t.getOldValue());
                tblEvents.refresh();
            }
        });
        colPlace.setOnEditCommit((CellEditEvent<Event, String> t) -> {
            if(validateLength(t.getNewValue())) {
                Event currEvent = t.getRowValue();
                currEvent.setPlace(t.getNewValue());
                eventManager.edit(currEvent);
                tblEvents.refresh();
            } else {
                Event currEvent = t.getRowValue();
                currEvent.setPlace(t.getOldValue());
                tblEvents.refresh();
            }
        });
        colPrice.setOnEditCommit((CellEditEvent<Event, Float> t) -> {
            if(t.getNewValue() != null && validatePrice(t.getNewValue())) {
                Event currEvent = t.getRowValue();
                currEvent.setTicketprice(t.getNewValue());
                eventManager.edit(currEvent);
                tblEvents.refresh();
            } else if(t.getNewValue() == null && 
                !lblError.getText().equals("* Field should be a positive number.")) {
                lblError.setText("* Field must not be empty.");
                lblError.setVisible(true);
                Event currEvent = t.getRowValue();
                currEvent.setTicketprice(t.getOldValue());
                tblEvents.refresh();
            }
        });
        colDescription.setOnEditCommit((CellEditEvent<Event, String> t) -> {
            if(validateLength(t.getNewValue())) {
                Event currEvent = t.getRowValue();
                currEvent.setDescription(t.getNewValue());
                eventManager.edit(currEvent);
                tblEvents.refresh();
            } else {
                Event currEvent = t.getRowValue();
                currEvent.setDescription(t.getOldValue());
                tblEvents.refresh();
            }
        });
        colClub.setOnEditCommit((CellEditEvent<Event, String> t) -> {
            Event currEvent = t.getRowValue();
            LOGGER.log(Level.INFO, t.getNewValue());
            Club club = UserManagerFactory.getUserManager().getUserByLogin(Club.class, t.getNewValue());
            currEvent.setClub(club);
            eventManager.edit(currEvent);
            tblEvents.refresh();
        });
        
        //Get data and add it to the table
        refreshData();
        
        if(userLogin != null) {
            //Find user events
            User userPrivilege = UserManagerFactory.getUserManager().getPrivilege(User.class, userLogin);
            switch(userPrivilege.getUserPrivilege()) {
                case CLIENT:
                    btnAddEvent.setDisable(true);
                    btnRemoveEvent.setDisable(true);
                    break;
                case ARTIST:
                    btnAddEvent.setDisable(true);
                    btnRemoveEvent.setDisable(true);
                    break;
                case CLUB:
                    btnAddEvent.setDisable(true);
                    btnRemoveEvent.setDisable(true);
                    break;
            }       
        }
        
        LOGGER.log(Level.INFO, "Successfully switched to Event Management window.");
    }

    /**
     * Removes an event.
     */
    @FXML
    private void handleButtonRemoveEvent() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete an Event?", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Event currEvent = (Event)tblEvents.getFocusModel().getFocusedItem();
            LOGGER.log(Level.INFO, "Removing event with id: {0}", currEvent.getId());
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
        tblEvents.refresh();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    private void refreshData() {
        Comparator<Event> byIdDescending = (Event e1, Event e2)->e2.getId().compareTo(e1.getId());
        if(userLogin != null) {
            //Find user events
            User userPrivilege = UserManagerFactory.getUserManager().getPrivilege(User.class, userLogin);
            switch(userPrivilege.getUserPrivilege()) {
                case ADMIN:
                    eventData = FXCollections.observableArrayList(eventManager.getAllEvents(new GenericType<List<Event>>(){}));
                    break;
                case CLIENT:
                    Client client = UserManagerFactory.getUserManager().getUserByLogin(Client.class, userLogin);
                    eventData = FXCollections.observableArrayList(client.getEvents());
                    break;
                case ARTIST:
                    Artist artist = UserManagerFactory.getUserManager().getUserByLogin(Artist.class, userLogin);
                    eventData = FXCollections.observableArrayList(artist.getEvents());
                    break;
                case CLUB:
                    Club club = UserManagerFactory.getUserManager().getUserByLogin(Club.class, userLogin);
                    eventData = FXCollections.observableArrayList(club.getEvents());
                    break;
            }       
        } else {
            eventData = FXCollections.observableArrayList(eventManager.getAllEvents(new GenericType<List<Event>>(){}));
        }
        
        Collections.sort(eventData, byIdDescending);
        tblEvents.setItems(eventData);
    }
    
    private Boolean validateLength(String s) {
        if (s.length() == 0) {
            lblError.setText("* Field must not be empty.");
            lblError.setVisible(true);
            return false;
        } else if (s.length() > 255){
            lblError.setText("* Must be less than 255 characters.");
            lblError.setVisible(true);
            return false;
        }
        lblError.setVisible(false);
        return true;
    }
    
    private Boolean validatePrice(Float price) {
        if(price < 0) {
            lblError.setText("* Field should be a positive number.");
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
                    lblError.setText("* Date must have format: dd/mm/yy\nand be possible.");
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
            try {                
                Float number = super.fromString(newPrice);
                lblError.setText("");
                lblError.setVisible(false);
                return number;
            } catch (NumberFormatException ex) {
                if(newPrice == null) {
                    lblError.setText("* Field must not be empty.");
                    lblError.setVisible(true);
                } else {
                    lblError.setText("* Field should be a positive number.");
                    lblError.setVisible(true);
                }
            }
            return null;
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
