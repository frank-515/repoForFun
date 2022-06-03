module com.example.sys {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires fastjson;
    requires lombok;

    opens com.example.sys to javafx.fxml;
    exports com.example.sys;
    opens com.example.sys.pojo; //加上这一句就好咯，为什么？
    exports com.example.sys.pojo;

}