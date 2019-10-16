package html.parsing.stock;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * A complete Java class to demonstrate the use of a JScrollPane.
 *
 * @author alvin alexander, devdaily.com.
 *
 */
public class JEditorPaneDemo {

    public static void main(String[] args) {
        new JEditorPaneDemo();
    }

    public JEditorPaneDemo() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JEditorPane jep = new JEditorPane();
                jep.setEditable(false);

                try {
                    jep.setPage("http://www.naver.com");
                } catch (IOException e) {
                    jep.setContentType("text/html");
                    jep.setText("<html>Could not load</html>");
                }

                JScrollPane scrollPane = new JScrollPane(jep);
                JFrame f = new JFrame("Test HTML");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.getContentPane().add(scrollPane);
                f.setPreferredSize(new Dimension(800, 600));
                f.setSize(new Dimension(800, 600));
                f.setVisible(true);
            }
        });
    }
}
