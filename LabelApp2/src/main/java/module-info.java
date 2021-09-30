module com.example.labelapp2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires json.simple;
    requires java.xml;
    requires java.desktop;

    opens com.example.labelapp2 to javafx.fxml;
    exports com.example.labelapp2;
}