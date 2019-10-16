/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author parsing-25
 */
public class JavaFXSwingAppBrowserJFrame implements Runnable {

    private static final int JFXPANEL_WIDTH_INT = 1024;
    private static final int JFXPANEL_HEIGHT_INT = 768;
    private static JFXPanel fxContainer;
    private WebEngine webEngine;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new JavaFXSwingAppBrowserJFrame());
    }

    @Override
    public void run() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }

        JFrame frame = new JFrame("JavaFX 2 in Swing");
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT + 50));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFXPanel jfxPanel1 = new JFXPanel();
        jfxPanel1.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        frame.getContentPane().add(jfxPanel1, BorderLayout.CENTER);

        JFXPanel jfxPanel2 = new JFXPanel();
        jfxPanel2.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, 50));
        frame.getContentPane().add(jfxPanel2, BorderLayout.SOUTH);

        //Platform.runLater(new Runnable() {
        Platform.runLater(() -> {
            WebView view = new WebView();
            webEngine = view.getEngine();

            jfxPanel1.setScene(new Scene(view));
        });
        frame.pack();

        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        jfxPanel2.setScene(new Scene(root));

        Platform.runLater(() -> {
            webEngine.load("http://www.google.com");
        });
        //frame.setLocationRelativeTo(null);
        //frame.setVisible(true);
    }

}
