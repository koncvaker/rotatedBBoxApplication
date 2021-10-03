package com.example.labelapp2;

import dataStorage.DataWorker;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

public class AnnotController {
    public Button setXMLReadPathBtn;
    public Button setPhotoReadPathBtn;
    public Button setJSONReadPathBtn;
    public Button setJSONWritePathBtn;
    public Button setJSONFileNameBtn;

    public Label photoReadPathLabel;
    public Label jsonReadPathLabel;
    public Label jsonWritePathLabel;
    public Label jsonFileNameLabel;
    public Label xmlReadPathLabel;
    public TextField jsonFilenameTextField;

    @FXML
    private Label imageFileName;
    @FXML
    private ImageView imageView;
    @FXML
    private Button JSONOpenerBtn;
    @FXML
    private Button imageShowerBtn;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button ImageOpenerBtn;
    @FXML
    private Button AnnotationShowBtn;
    @FXML
    private Button AnnotationClearBtn;
    @FXML
    private Pane RectanglePane;
    @FXML
    private CheckBox segmentationCheckbox;




    private String readXMLPath;
    private String writeJSONPath;
    private String readImgFolderPath;
    private String readJSONPath;
    private String writeJSONFileName;
    private int imgId;
    private String[] readImages;
    private final DataWorker dataStorage = new DataWorker();
    public double imgWidth;
    public double imgHeight;


    @FXML
    protected void getXMLReadPath(){
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        readXMLPath = file.getPath();
        xmlReadPathLabel.setText(readXMLPath);
        dataStorage.setXMLReadPath(readXMLPath);

    }
    @FXML
    protected void getPhotoReadPath(){
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        readImgFolderPath = file.getPath();
        photoReadPathLabel.setText(readImgFolderPath);
    }
    @FXML
    protected void getJSONReadPath(){
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        readJSONPath = file.getPath();
        jsonReadPathLabel.setText(readJSONPath);
        dataStorage.setJSONReadPath(readJSONPath);
    }
    @FXML
    protected void getJSONWritePath(){
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        writeJSONPath = file.getPath();
        jsonWritePathLabel.setText(writeJSONPath);
        dataStorage.setJSONWritePath(writeJSONPath);
    }
    @FXML
    protected void getJSONFilenamePath(){
        writeJSONFileName = jsonFilenameTextField.getText();
        jsonFileNameLabel.setText(writeJSONFileName);
        dataStorage.setJSONFileName(writeJSONFileName);
    }


    @FXML
    protected void JSONRead() {
        dataStorage.readJSON();
    }
    @FXML
    protected void XMLRead(){
        dataStorage.readAllXML();
    }
    @FXML
    protected void JSONWrite(){
        dataStorage.writeDataToJSON();
    }
    @FXML
    protected void printData(){
        System.out.println("Printing storage:");
        dataStorage.printAll();
    }
    @FXML
    protected void FolderOpener() {
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        SetFileList(file);
        System.out.println(file);
    }

    //Image showing stuff
    @FXML
    protected void StartImage() {
        imgId = 0;
        SetImage();
    }
    @FXML
    protected void NextImage() {
        if (imgId < readImages.length -1) {
            imgId = imgId + 1;
            SetImage();
        } else {
            return;
        }
    }
    @FXML
    protected void PrevImage() {
        if (imgId > 0) {
            imgId = imgId - 1;
            SetImage();
        } else {
            return;
        }
    }
    private void SetImage() {
        RemoveRectangles();
        if (readImages.length > 0) {
            FileInputStream input = null;
            try {
                input = new FileInputStream(readImgFolderPath + File.separator + readImages[imgId]);
                Image image = new Image(input);
                imageView.setImage(image);
                imageFileName.setText(readImages[imgId]);
                double ratio1 = imageView.getBoundsInParent().getWidth()/imageView.getImage().getWidth();
                double ratio2 = imageView.getBoundsInParent().getHeight()/imageView.getImage().getHeight();
                GenerateRectangles(readImages[imgId],Math.min(ratio1,ratio2));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
    private void SetFileList(File files){
        File[] listOfFiles = files.listFiles();
        Vector<String> tempVector = new Vector<String>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].isHidden()) {
                tempVector.add(listOfFiles[i].getName());
                System.out.println(listOfFiles[i].getName());
            }
        }
        System.out.println(tempVector.size());
        readImages = tempVector.toArray(new String[tempVector.size()]);
        readImgFolderPath = files.getPath();
        imageShowerBtn.setDisable(tempVector.size() <= 0 || readImgFolderPath.length() <= 0);
    }

    //Annotations stuff

    private void GenerateRectangles(String filename,double ratio){
        if(!segmentationCheckbox.isSelected()){
            Vector<Rectangle> rectangleVector = dataStorage.storage.getBBoxRectangles(filename,ratio);
            for (Rectangle i : rectangleVector){
                RectanglePane.getChildren().add(i);
            }
        }
        else {
            Vector<Polygon> polygonVector = dataStorage.storage.getBBoxSegmentations(filename,ratio);
            for(Polygon i : polygonVector){
                RectanglePane.getChildren().add(i);
            }
        }



    }
    @FXML
    protected void RemoveRectangles(){
        RectanglePane.getChildren().removeAll();
        RectanglePane.getChildren().clear();

    }




}