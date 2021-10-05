module com.example.labelapp2 {


    requires org.kordamp.ikonli.javafx;
    requires json.simple;
    requires java.xml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    opens com.example.labelapp2 to javafx.fxml;
    exports com.example.labelapp2;
}