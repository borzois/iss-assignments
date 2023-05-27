package org.example.teledon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.teledon.controller.MainController;
import org.example.teledon.repository.*;
import org.example.teledon.service.MainService;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainClass extends Application {
    MainService service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
//        initSessionFactory();
        // load rsc
        Class.forName("org.sqlite.JDBC"); // load driver

        Properties properties = new Properties();
        Logger logger = LogManager.getLogger();
        try {
            properties.load(new FileReader("studio.config"));
            logger.info("loaded config file");
        } catch (IOException e) {
            logger.error("could not find config file");
        }

        UserDBRepository userDBRepository = new UserDBRepository(properties);
        BookingDBRepository bookingDBRepository = new BookingDBRepository(properties);
//        BookingORMRepository bookingORMRepository = new BookingORMRepository(sessionFactory);
//        CaseORMRepository caseORMRepository = new CaseORMRepository(sessionFactory);
//        caseORMRepository.findAll().forEach(System.out::println);
        service = new MainService(userDBRepository, bookingDBRepository);

        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.setHeight(510);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Studio");
        primaryStage.show();

//        closeSessionFactory();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("views/MainView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        MainController mainController = fxmlLoader.getController();
        mainController.initData(service);
    }

    static SessionFactory sessionFactory;
    static void initSessionFactory() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml") // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("session factory exception: "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void closeSessionFactory(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }

}