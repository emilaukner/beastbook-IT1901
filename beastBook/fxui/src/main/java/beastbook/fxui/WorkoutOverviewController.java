package beastbook.fxui;

import beastbook.core.User;
import beastbook.core.Workout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;



/**
 * Controller for the WorkoutOverview screen.
 */
public class WorkoutOverviewController extends AbstractController {
  @FXML
  private AnchorPane rootPane;

  @FXML
  private Button backButton;

  @FXML
  private Button openButton;

  @FXML
  private Button deleteButton;

  @FXML
  private Text exceptionFeedback;

  @FXML
  private TableView<Workout> workoutOverview = new TableView<Workout>();
  private TableColumn<Workout, String> workoutNameColumn;
  //private Workout workout = new Workout();
  private String selectedWorkoutName;
  private User user;

  public void initialize() {
    loadTable();
  } 
    
  /**
  * Creates a table view with a column for workout name and
  * adds the users workouts to the table view.
  */
  public void loadTable() {
    setWorkouts();
    workoutOverview.getColumns().clear();
    workoutNameColumn = new TableColumn<Workout, String>("Workout name:");
    workoutNameColumn.setCellValueFactory(new PropertyValueFactory<Workout, String>("name"));
    workoutOverview.getColumns().add(workoutNameColumn);
    workoutOverview.getItems().setAll(allWorkouts);
    setColumnsSize();
  }

  @FXML
  private void workoutSelectedListener() throws IOException {
    workout = workoutOverview.getSelectionModel().getSelectedItem();
    if (workout != null) {
      exceptionFeedback.setText("");
      openButton.setDisable(false);
      deleteButton.setDisable(false);
    } else {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
    }
  }

  public void setWorkout(Workout workout) {
    this.workout = workout;
  }

  public Workout getWorkout() {
    return this.workout;
  }

  public void addToAllWorkouts(Workout workout) {
    allWorkouts.add(workout);
  }

  List<Workout> getAllWorkouts() {
    return new ArrayList<>(allWorkouts);
  }

  Workout getTable(int row) {
    return workoutOverview.getItems().get(row);
  }

  public TableView<Workout> getWorkoutOverview() {
    return workoutOverview;
  }

  private void setColumnsSize() {
    workoutNameColumn.setPrefWidth(150);    
  }

  @Override
  void loadHome(ActionEvent event, String username) throws IOException {
    super.loadHome(event, user.getUserName());
  }

  @FXML
  void loadWorkout(ActionEvent event) throws IOException {
    try {
      exceptionFeedback.setText("");
      WorkoutController workoutController = new WorkoutController();
      FXMLLoader fxmlLoader = new FXMLLoader(
          this.getClass().getResource("/beastbook.fxui/Workout.fxml")
      );
      fxmlLoader.setController(workoutController);
      workoutController.setUser(user);
      workoutController.setWorkout(this.getWorkout());
    
      AnchorPane pane =  fxmlLoader.load();
      rootPane.getChildren().setAll(pane);
    } catch (Exception e) {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
      exceptionFeedback.setText("No workout is selected!");
    }
  }

  @FXML
  void deleteWorkout() throws IllegalStateException, IOException {
    user.removeWorkout(workout);
    loadTable();
    exceptionFeedback.setText("Workout deleted!");
    user.saveUser();
    openButton.setDisable(true);
    deleteButton.setDisable(true);
  }
    
  void setUser(User user) {
    this.user = user;
  }

  void setWorkouts() {
    this.allWorkouts = user.getWorkouts();
  }
}
