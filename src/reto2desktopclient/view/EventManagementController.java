/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.view;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javax.ws.rs.core.GenericType;
import reto2desktopclient.client.EventManagerFactory;
import reto2desktopclient.model.Event;

/**
 * TODO: Remove id column. Remove MusicGenre column. Add Name column. Validate
 * item not col or row in table.
 *
 * @author Martin Angulo
 */
public class EventManagementController {

    private static final Logger LOGGER = Logger.getLogger(EventManagementController.class.getName());

    @FXML
    private Stage stage;

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

        //Set cell editing properties
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(table -> table.getRowValue().setName(table.getNewValue()));
        
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDate.setCellFactory((TableColumn<Event, Date> param) -> new DateEditingCell());
        
        colPlace.setCellValueFactory(new PropertyValueFactory<>("place"));
        colPlace.setCellFactory(TextFieldTableCell.forTableColumn());
        
        colPrice.setCellValueFactory(new PropertyValueFactory<>("ticketprice"));
        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        
        //Get data and add it to the table
        eventData = FXCollections.observableArrayList(EventManagerFactory.getEventManager().getAllEvents(new GenericType<List<Event>>() {
        }));
        tblEvents.setItems(eventData);
        //Make the table editable
        tblEvents.setEditable(true);
        tblEvents.getSelectionModel().cellSelectionEnabledProperty().set(true);

        LOGGER.log(Level.INFO, "Successfully switched to Event Management window.");
    }

    /**
     * Removes an event.
     */
    @FXML
    private void handleButtonRemoveEvent() {
        LOGGER.log(Level.INFO, "Removing event.");
    }

    /**
     * Validates input and adds an event.
     */
    @FXML
    private void handleButtonAddEvent() {
        LOGGER.log(Level.INFO, "Adding event.");
    }

    /**
     * Switches the scene from Event Management to AdminMainMenu.
     */
    @FXML
    private void handleButtonBack() {
        try {
            LOGGER.log(Level.INFO, "Redirecting to AdminMainMenu window.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reto2desktopclient/view/AdminMainMenu.fxml"));
            Parent root = (Parent) loader.load();
            //Getting window controller.
            AdminMainMenuController controller = (loader.getController());
            controller.setStage(stage);
            //Initializing stage.
            controller.initStage(root);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not switch to AdminMainMenu window: {0}", ex.getMessage());
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    class DateEditingCell extends TableCell<Event, Date> {
        private DatePicker datePicker;

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createDatePicker();
                setText(null);
                setGraphic(datePicker);
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getDate().toString());
            setGraphic(null);
        }

        @Override
        public void updateItem(Date item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (datePicker != null) {
                        datePicker.setValue(getDate());
                    }
                    setText(null);
                    setGraphic(datePicker);
                } else {
                    setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
                    setGraphic(null);
                }
            }
        }

        private void createDatePicker() {
            datePicker = new DatePicker(getDate());
            datePicker.setOnAction((e) -> {
                commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            });
        }

        private LocalDate getDate() {
            if(getItem() != null) {
                return getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                return LocalDate.now();
            }
        }
    }
}
