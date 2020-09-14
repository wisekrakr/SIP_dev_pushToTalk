package com.wisekrakr.communiwise.gui.fx;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;


/**
 * Initializes a new JFXPanel. The FXMLoader first loads a fxml file, and this controller gets set to it.
 * Runs the createScene method that sets the root to the scene and the scene to the controller.
 *
 * * This is done like this so that the controller wont be set in the fxml file, but in its runner class
 * * and we can pass parameters to its constructor.
 * * Control the app with this controller and fill the components with the parameters that were passed (optional)
 */
 public class ControllerJFXPanel extends JFXPanel implements ControllerContext {

    private Parent root;

    private void createScene() {
        Scene scene = new Scene(root);

        setScene(scene);
    }

    /**
     * @param fxmlPath path to a fxml file in resources.
     * @return a new JFXPanel for a JFrame to add onto itself
     */
    @Override
    public ControllerJFXPanel initialize(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml" + fxmlPath));
        loader.setController(this);
        try {
            loader.load();
        } catch (Throwable t) {
            throw new IllegalArgumentException("Could not load fxml file: " + fxmlPath, t);
        }
        root = loader.getRoot();
        Platform.runLater(this::createScene);
        return this;
    }

    /**
     * Initialized in the controller class and will run immediately after the controller is made.
     * It will hold the components that need to be filled at initialization, components like: labels, buttons, etc.
     */
    @Override
    public void initComponents() {

    }
}
