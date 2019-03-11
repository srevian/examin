package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Batch;
import model.Course;
import model.Student;
import service.BatchService;
import service.CourseService;
import service.StudentService;
import util.UISetterUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Controller class for StudentsList.fxml
 *
 * @author Avik Sarkar
 */
public class StudentsListController {

    /*--------------------Declaration and initialization of variables--------------------*/

    private StudentService studentService;

    private CourseService courseService;

    private BatchService batchService;

    private List<Batch> listOfBatches;

    private List<Student> listOfStudents;

    private List<Course> listOfCourses;

    private FilteredList<Student> studentFilteredItems;

    private ObservableList<Student> studentObsList;

    @FXML
    private GridPane studentListGridPane;

    @FXML
    private Label titleLabel;

    @FXML
    private Button importButton;

    @FXML
    private ComboBox<String> batchNameComboBox;

    @FXML
    private ComboBox<String> degreeComboBox;

    @FXML
    private ComboBox<String> disciplineComboBox;

    @FXML
    private ComboBox<String> semesterComboBox;

    @FXML
    private TableView<Student> studentTableView;

    @FXML
    private TextField searchTextField;

    @FXML
    private TableColumn<Student, String> regIdCol;

    @FXML
    private TableColumn<Student, String> rollNoCol;

    @FXML
    private TableColumn<Student, String> firstNameCol;

    @FXML
    private TableColumn<Student, String> middleNameCol;

    @FXML
    private TableColumn<Student, String> lastNameCol;

    @FXML
    private TableColumn<Student, String> guardianNameCol;

    @FXML
    private TableColumn<Student, String> contactNoCol;

    @FXML
    private TableColumn<Student, String> degreeCol;

    @FXML
    private TableColumn<Student, String> disciplineCol;

    @FXML
    private TableColumn<Student, String> semesterCol;

    @FXML
    private TableColumn<Student, String> batchCol;

    @FXML
    private TableColumn<Student, String> regYearCol;


    /*------------------------------End of initialization-------------------------------*/

    /**
     *  This method is called once the FXML document is loaded.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    public void initialize() {

        studentService = new StudentService();
        courseService = new CourseService();
        batchService = new BatchService();

        //initialize this for the studentTableView
        studentObsList = FXCollections.observableArrayList();

        //get the list of courses available in the db
        Task<List<Course>> coursesTask = courseService
                .getCoursesTask("");
        new Thread(coursesTask).start();

        coursesTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //initialize columns of the studentTableView
                initCol();

                listOfCourses = coursesTask.getValue();

                //only if there's any course in the db
                if (!listOfCourses.isEmpty()) {

                    List<String> items = new ArrayList<>();

                    for (Course course : listOfCourses) {

                        //add only unique degree items to degree combobox
                        if (!items.contains(course.getDegree()))

                            items.add(course.getDegree());
                    }

                    //choosing this will display students from all degrees
                    items.add("all");

                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    degreeComboBox.setItems(options);
                }
            }
        });

    }


    /**
     * Callback method for DegreeComboBox.
     * Clears all other comboBoxes,table items and sets the disciplineComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDegreeComboBox() {

        disciplineComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getItems().clear();

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        //clears the tableView
        studentObsList.clear();

        /*
        If 'all' is chosen , disable other comboBoxes
        and display all students data.
         */
        if (degreeComboBox.getValue().equals("all")) {
            disciplineComboBox.setDisable(true);
            batchNameComboBox.setDisable(true);
            semesterComboBox.setDisable(true);
            populateTable();
        } else if (degreeComboBox.getValue() != null) {

            disciplineComboBox.setDisable(false);
            batchNameComboBox.setDisable(false);
            semesterComboBox.setDisable(false);

            //Add every discipline which comes under the selected degree
            if (!listOfCourses.isEmpty()) {

                List<String> items = new ArrayList<>();
                for (Course course : listOfCourses) {

                    if (course.getDegree().equals(degreeComboBox.getValue()))

                        //add the disciplines to the discipline comboBox for particular degree
                        if (!items.contains(course.getDiscipline()))
                            items.add(course.getDiscipline());
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                disciplineComboBox.setItems(options);
            }
        }
    }

    /**
     * Callback method for disciplineComboBox.
     * Clears batchNameComboBox,semesterComboBox & table items and sets
     * the batchNameComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDisciplineComboBox() {

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        studentObsList.clear();

        //only if a discipline is selected
        if (disciplineComboBox.getValue() != null) {

            final String additionalQuery = "where v_degree=? and v_discipline =?";

            //get all batches for particular degree and discipline
            Task<List<Batch>> batchesTask = batchService.getBatchesTask(additionalQuery, degreeComboBox.getValue()
                    , disciplineComboBox.getValue());
            new Thread(batchesTask).start();

            batchesTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    listOfBatches = batchesTask.getValue();

                    if (!listOfBatches.isEmpty()) {

                        List<String> items = new ArrayList<>();

                        for (Batch batch : listOfBatches) {

                            //Add unique batch names to the batchComboBox
                            if (!items.contains(batch.getBatchName()))
                                items.add(batch.getBatchName());
                        }
                        ObservableList<String> options = FXCollections.observableArrayList(items);
                        batchNameComboBox.setItems(options);
                    }
                }
            });

        }

    }

    /**
     * Callback method for batchNameComboBox.
     * Clears semesterComboBox & table items and sets
     * the semesterComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleBatchNameComboBox() {

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();
        studentObsList.clear();

        if (batchNameComboBox.getValue() != null) {
            if (!listOfCourses.isEmpty()) {

                List<String> items = new ArrayList<>();
                int totalSemesters = 0;
                /*
                Get the total no.of semesters for the selected Degree,discipline &
                set the semesterComboBox with semester values from 1 to total no.
                 */
                for (Course course : listOfCourses) {
                    if (course.getDegree().equals(degreeComboBox.getValue())
                            && course.getDiscipline().equals(disciplineComboBox.getValue()))
                        totalSemesters = Integer.parseInt(course.getDuration());
                }
                for (int i = 1; i <= totalSemesters; i++)
                    items.add(Integer.toString(i));
                ObservableList<String> options = FXCollections.observableArrayList(items);
                semesterComboBox.setItems(options);

            }
        }
    }

    /**
     * Callback method for semesterComboBox.
     * Clears table items and populate the tableView.
     * the semesterComboBoxes.
     */
    @FXML
    private void handleSemesterComboBox() {

        studentObsList.clear();

        if (semesterComboBox.getValue() != null) {
            //System.out.println(event.toString());
            titleLabel.setText("List of " + batchNameComboBox.getValue() + " " + degreeComboBox.getValue() +
                    " " + disciplineComboBox.getValue() + " Semester" + semesterComboBox.getValue() +
                    " students");
            populateTable();
        }

    }

    /**
     * This method initializes the columns of the Student Table.
     */
    private void initCol() {

        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        regIdCol.setCellValueFactory(new PropertyValueFactory<>("regId"));
        rollNoCol.setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        guardianNameCol.setCellValueFactory(new PropertyValueFactory<>("guardianName"));
        contactNoCol.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        degreeCol.setCellValueFactory(new PropertyValueFactory<>("degree"));
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("currSemester"));
        disciplineCol.setCellValueFactory(new PropertyValueFactory<>("discipline"));
        batchCol.setCellValueFactory(new PropertyValueFactory<>("batchName"));
        regYearCol.setCellValueFactory(new PropertyValueFactory<>("regYear"));

    }

    /**
     * This method populates and updates the Student Table.
     */
    private void populateTable() {

        String additionalQuery = "";

        Task<List<Student>> studentsTask;


        if (degreeComboBox.getValue().equals("all")) {

            studentsTask = studentService.getStudentTask(additionalQuery);
        } else {

            additionalQuery = "where v_degree=? and v_discipline=? " +
                    "and v_batch_name=? and v_curr_semester=?";
            studentsTask = studentService.getStudentTask(additionalQuery
                    , degreeComboBox.getValue(), disciplineComboBox.getValue()
                    , batchNameComboBox.getValue(), semesterComboBox.getValue());
        }

        new Thread(studentsTask).start();

        studentsTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                listOfStudents = studentsTask.getValue();
                studentObsList.setAll(listOfStudents);
                studentFilteredItems = new FilteredList<>(studentObsList, null);
                searchTextField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        studentFilteredItems.setPredicate(new Predicate<>() {
                            @Override
                            public boolean test(Student student) {

                                if (newValue == null || newValue.isEmpty())
                                    return true;
                                String lowerCaseFilter = newValue.toLowerCase();

                                if (student.getFirstName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (student.getMiddleName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (student.getLastName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (student.getRollNo().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (student.getRegId().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (student.getDegree().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (student.getDiscipline().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (student.getCurrSemester().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (student.getBatchName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (student.getRegYear().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (student.getGuardianName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (student.getContactNo().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                return false;
                            }
                        });
                    }
                });

                studentTableView.setItems(studentFilteredItems);
            }
        });

    }

    /**
     * Callback method for ImportButton to import the students from the Csv file.
     * @throws IOException Load exception while loading the Fxml document.
     */
    @FXML
    private void handleImportButtonAction() throws IOException {

        //create a new stage first
        Stage importStudentListModalWindow = new Stage();

        //get the stage where the Import Button is situated
        Stage parentStage = (Stage) importButton.getScene().getWindow();

        FXMLLoader loader = UISetterUtil.setModalWindow("/view/ImportStudentCSVModal.fxml"
                , importStudentListModalWindow, parentStage, "Import Student List");

        //get the controller
        ImportStudentCSVModalController importStudentCSVModalController = loader.getController();

        /*
        showAndWait() since we need to get the status for updating the table from the Import Student controller.
        The application thread is blocked here and goes to execute the Import modal operations and only when the Import
        modal window is closed , the tableUpdateStatus is sent from the Import modal controller.
         */
        importStudentListModalWindow.showAndWait();

        boolean tableUpdateStatus = importStudentCSVModalController.getTableUpdateStatus();
        if (tableUpdateStatus && degreeComboBox.getValue() != null){

            if(degreeComboBox.getValue().equals("all"))
                populateTable();
            else if(disciplineComboBox.getValue() != null && batchNameComboBox.getValue() != null
                    && semesterComboBox.getValue() != null)
                populateTable();
        }

    }

    /**
     * Callback method
     * @throws IOException
     */
    @FXML
    private void handleAddStudentButtonAction() throws IOException {
        StackPane contentStackPane = (StackPane) studentListGridPane.getParent();
        Parent studentRegistrationFxml = FXMLLoader.load(getClass()
                .getResource("/view/StudentRegistration.fxml"));
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(studentRegistrationFxml);
    }

    @FXML
    private void handleMouseClickOnTableViewItem() throws IOException {
        Student student = studentTableView.getSelectionModel().getSelectedItem();

        if (student != null) {

            Stage viewStudentModalWindow = new Stage();
            Stage parentStage = (Stage) importButton.getScene().getWindow();
            FXMLLoader loader = UISetterUtil.setModalWindow("/view/ViewStudentModal.fxml"
                    , viewStudentModalWindow, parentStage, "Student");
            ViewStudentModalController viewStudentModalController = loader.getController();
            viewStudentModalController.setStudentPojo(student);
            viewStudentModalWindow.showAndWait();

            if (viewStudentModalController.getStudentDeletedStatus())
                studentObsList.remove(student);
        }
    }
}
