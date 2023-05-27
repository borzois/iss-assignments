module org.example.teledon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.apache.logging.log4j;
    requires java.sql;

    opens org.example to javafx.fxml, org.apache.logging.log4j, java.sql, javafx.graphics, javafx.base;
    exports org.example to javafx.graphics;
    exports org.example to javafx.fxml;
    exports org.example to javafx.base;
    exports org.example;
}