package com.example.sys;

import java.sql.Date;
import com.example.sys.pojo.Staff;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.format.TextStyle;

public class getStaff extends Application {
    private Stage stage = new Stage();
    private Staff s;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        pane.setLayoutX(340);
        pane.setLayoutX(720);

        Label nameLabel = new Label("name");
        TextField nameTf = new TextField();

        Label genderLabel = new Label("gender");
        TextField genderTf = new TextField();

        Label phoneNumberLabel = new Label("phone number");
        TextField phoneNumberTf = new TextField();

        Label IDLabel = new Label("ID");
        TextField IDTf = new TextField();

        Label addressLabel = new Label("address");
        TextField addressTf = new TextField();

        Label qqLabel = new Label("qq");
        TextField qqTf = new TextField();

        Label birthLabel = new Label("birth");
        TextField birthTf = new TextField();

        Label postcodeLabel = new Label("postcode");
        TextField postcodeTf = new TextField();

        Label classifyLabel = new Label("classify");
        TextField classifyTf = new TextField();

        Button ok = new Button("ok");
        Button cancel = new Button("cancel");
        HBox buttons = new HBox(ok, cancel);

        GridPane layout = new GridPane();
        layout.add(nameLabel, 0, 0);
        layout.add(nameTf, 1, 0);
        layout.add(genderLabel, 0, 1);
        layout.add(genderTf, 1, 1);
        layout.add(phoneNumberLabel, 0, 2);
        layout.add(phoneNumberTf, 1, 2);
        layout.add(IDLabel, 0, 3);
        layout.add(IDTf, 1, 3);
        layout.add(addressLabel, 0, 4);
        layout.add(addressTf, 1, 4);
        layout.add(qqLabel, 0, 5);
        layout.add(qqTf, 1, 5);
        layout.add(birthLabel, 0, 6);
        layout.add(birthTf, 1, 6);
        layout.add(postcodeLabel, 0, 7);
        layout.add(postcodeTf, 1, 7);
        layout.add(classifyLabel, 0, 8);
        layout.add(classifyTf, 1, 8);
        layout.add(buttons, 0, 9);
        s = new Staff();
        ok.setOnAction(event -> {
            s.setName(nameTf.getText());
            String[] yyyymmdd = birthTf.getText().split("-");
            Date date = new Date (Integer.parseInt(yyyymmdd[0]), Integer.parseInt(yyyymmdd[0]), Integer.parseInt(yyyymmdd[0]));
            s.setBirth(date);
            s.setPhoneNumber(phoneNumberTf.getText());
            s.setAddress(addressTf.getText());
            s.setQq(qqTf.getText());
            s.setClassify(classifyTf.getText());
            s.setGender(genderTf.getText());
            s.setPostcode(postcodeTf.getText());
            s.setID(IDTf.getText());
        });
        pane.getChildren().add(layout);
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }
    public Staff getStaff() {
        return this.s;
    }
}
