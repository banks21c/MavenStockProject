package html.parsing.stock;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class JavaFxBrowser implements Runnable {

    private WebEngine webEngine;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new JavaFxBrowser());

//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowGUI();
//            }
//        });        
    }

    public void loadURL(final String url) {
        Platform.runLater(() -> {
            webEngine.load(url);
        });
    }

    @Override
    public void run() {
        // setup UI
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(1024, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFXPanel jfxPanel = new JFXPanel();
        frame.getContentPane().add(jfxPanel);
        frame.pack();

        Platform.runLater(() -> {
            WebView view = new WebView();
            webEngine = view.getEngine();

            jfxPanel.setScene(new Scene(view));
        });

        loadURL("http://www.google.com");
    }
}
