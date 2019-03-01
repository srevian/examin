package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Batch;
import model.Course;
import model.Student;
import service.BatchService;
import service.CourseService;
import service.StudentService;
import util.ValidatorUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentRegistrationController {

    private CourseService courseService;

    private BatchService batchService;

    private StudentService studentService;

    private List<Course> listOfCourses;

    private List<Batch> listOfBatches;

    @FXML
    private ComboBox<String> batchNameComboBox;

    @FXML
    private ComboBox<String> degreeComboBox;

    @FXML
    private ComboBox<String> disciplineComboBox;

    @FXML
    private ComboBox<String> semesterComboBox;

    @FXML
    private Button submitButton;

    @FXML
    private TextField regYearTextField;

    @FXML
    private TextField rollNoTextField;

    @FXML
    private TextField regIdTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField middleNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField contactNoTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField guardianNameTextField;

    @FXML
    private TextField motherNameTextField;

    @FXML
    private TextField guardianContactNoTextField;

    @FXML
    private ImageView statusImageView;

    @FXML
    private Button addAnotherButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label statusLabel;

    @FXML
    private AnchorPane statusAnchorPane;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private void initialize() {
        courseService = new CourseService();
        batchService = new BatchService();
        studentService = new StudentService();
        listOfCourses = courseService.getCourseData();
        if (!listOfCourses.isEmpty()) {
            List<String> items = new ArrayList<>();
            for (Course course : listOfCourses) {
                if (!items.contains(course.getDegree()))
                    items.add(course.getDegree());
            }
            ObservableList<String> options = FXCollections.observableArrayList(items);
            degreeComboBox.setItems(options);
        }
        genderChoiceBox.setItems(FXCollections.observableArrayList("Male"
                , "Female", "Others"));

    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleBatchNameComboBox() {
        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        if (batchNameComboBox.getValue() != null) {
            //System.out.println(event.toString());
            if (!listOfCourses.isEmpty()) {

                List<String> items = new ArrayList<>();
                int totalSemesters = 0;
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

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDegreeComboBox() {

        disciplineComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getItems().clear();

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        if (degreeComboBox.getValue() != null) {
            if (!listOfCourses.isEmpty()) {
                List<String> items = new ArrayList<>();
                for (Course course : listOfCourses) {
                    if (course.getDegree().equals(degreeComboBox.getValue()))
                        if (!items.contains(course.getDiscipline()))
                            items.add(course.getDiscipline());
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                disciplineComboBox.setItems(options);

            }
        }
    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDisciplineComboBox() {

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        if (disciplineComboBox.getValue() != null) {
            //System.out.println(event.toString());
            String additionalQuery = "where v_degree=? and v_discipline =?";
            listOfBatches = batchService.getBatchData(additionalQuery, degreeComboBox.getValue()
                    , disciplineComboBox.getValue());
            if (!listOfBatches.isEmpty()) {
                List<String> items = new ArrayList<>();
                for (Batch batch : listOfBatches) {
                    if (!items.contains(batch.getBatchName()))
                        items.add(batch.getBatchName());
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                batchNameComboBox.setItems(options);

            }
        }
    }



    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubmitButtonAction() {
        if (validate()) {

            mainGridPane.setOpacity(0.5);
            statusAnchorPane.setVisible(true);
            progressIndicator.setVisible(true);

            String degree = degreeComboBox.getValue();
            String discipline = disciplineComboBox.getValue();
            String batchName = batchNameComboBox.getValue();
            String currSemester = semesterComboBox.getValue();
            String regYear = regYearTextField.getText().trim();
            String regId = regIdTextField.getText().trim();
            String rollNo = rollNoTextField.getText().trim();
            String firstName = firstNameTextField.getText().trim();
            String middleName = middleNameTextField.getText().trim();
            String lastName = lastNameTextField.getText().trim();
            String dob = dobDatePicker.getValue().toString();
            String gender = genderChoiceBox.getValue();
            String email = emailTextField.getText().trim();
            String contactNo = contactNoTextField.getText().trim();
            String address = addressTextField.getText().trim();
            String guardianName = guardianNameTextField.getText().trim();
            String motherName = motherNameTextField.getText().trim();
            String guardianContactNo = guardianContactNoTextField.getText().trim();
            String batchId = "";
            String courseId = "";
            for (Batch batch : listOfBatches) {

                if (batch.getDiscipline().equals(discipline) && batch.getDegree().equals(degree)
                        && batch.getBatchName().equals(batchName)) {
                    batchId = batch.getBatchId();
                    courseId = batch.getCourseId();
                }

            }


            boolean status = studentService.addStudentToDatabase(new Student(firstName, middleName
                    , lastName, dob, gender, regYear, email, address, motherName
                    , guardianContactNo, regId, rollNo, contactNo, guardianName
                    , batchId, courseId, currSemester, batchName, discipline, degree, ""
                    , ""));

            progressIndicator.setVisible(false);
            if (status) {
                statusImageView.setImage(new Image("/png/success.png"));
                statusLabel.setText("Added Successfully!");
            } else {
                statusImageView.setImage(new Image("/png/error.png"));
                statusLabel.setText("Failed!");
            }

        }
    }


    private boolean validate() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (degreeComboBox.getValue() == null) {
            alert.setContentText("Please select a degree!");
            alert.show();
            return false;
        } else if (disciplineComboBox.getValue() == null) {
            alert.setContentText("Please select a discipline!");
            alert.show();
            return false;
        } else if (batchNameComboBox.getValue() == null) {
            alert.setContentText("Please select a batch!");
            alert.show();
            return false;
        } else if (semesterComboBox.getValue() == null) {
            alert.setContentText("Please select a semester!");
            alert.show();
            return false;
        } else if (regYearTextField.getText().isEmpty()) {
            alert.setContentText("Registration Year cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateRegYear(batchNameComboBox.getValue()
                ,regYearTextField.getText())) {
            alert.setContentText("Invalid Registration Year or not within batch range!");
            alert.show();
            return false;
        } else if (regIdTextField.getText().isEmpty()) {
            alert.setContentText("Registration ID cannot be empty!");
            alert.show();
            return false;
        } else if (rollNoTextField.getText().isEmpty()) {
            alert.setContentText("Roll No. cannot be empty!");
            return false;
        } else if (firstNameTextField.getText().isEmpty()) {
            alert.setContentText("First Name cannot be empty!");
            alert.show();
            return false;
        } else if (dobDatePicker.getValue() == null) {
            alert.setContentText("Please choose a date of birth!");
            alert.show();
            return false;
        } else if (genderChoiceBox.getValue() == null) {
            alert.setContentText("Please select a gender!");
            alert.show();
            return false;
        } else if (emailTextField.getText().isEmpty()) {
            alert.setContentText("Email ID cannot be empty!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateEmail(emailTextField.getText())) {
            alert.setContentText("Invalid Email ID !");
            alert.show();
            return false;
        }
        else if (contactNoTextField.getText().isEmpty()) {
            alert.setContentText("Contact No. cannot be empty!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateContactNo(contactNoTextField.getText())) {
            alert.setContentText("Invalid Contact No.!");
            alert.show();
            return false;
        }
        else if (addressTextField.getText().isEmpty()) {
            alert.setContentText("Address cannot be empty!");
            alert.show();
            return false;
        } else if (guardianNameTextField.getText().isEmpty()) {
            alert.setContentText("Guardian/Father's Name cannot be empty!");
            alert.show();
            return false;
        } else if (guardianContactNoTextField.getText().isEmpty()) {
            alert.setContentText("Guardian Contact No. cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateContactNo(guardianContactNoTextField
                .getText())) {
            alert.setContentText("Invalid Guardian Contact No.!");
            alert.show();
            return false;
        }
        return true;
    }

    @FXML
    private void handleAddAnotherButtonAction(){
        statusAnchorPane.setVisible(false);
        mainGridPane.setOpacity(1);
        firstNameTextField.clear();
        middleNameTextField.clear();
        lastNameTextField.clear();
        degreeComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getSelectionModel().clearSelection();
        regYearTextField.clear();
        regIdTextField.clear();
        rollNoTextField.clear();
        genderChoiceBox.getSelectionModel().clearSelection();
        dobDatePicker.getEditor().clear();
        emailTextField.clear();
        addressTextField.clear();
        contactNoTextField.clear();
        guardianNameTextField.clear();
        motherNameTextField.clear();
        guardianContactNoTextField.clear();
    }

    @FXML
    private void handleCancelButtonAction() throws IOException {
        statusAnchorPane.setVisible(false);
        mainGridPane.setOpacity(1);
        Pane listPane = (Pane)rootAnchorPane.getParent();
        Parent studentRegistrationFxml = FXMLLoader.load(getClass()
                .getResource("/view/StudentsList.fxml"));
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentRegistrationFxml);
    }
}