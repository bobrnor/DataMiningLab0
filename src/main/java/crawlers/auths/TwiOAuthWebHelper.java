package crawlers.auths;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;


public class TwiOAuthWebHelper {
    private Stage m_stage;
    private OAuthWebHelperListener m_listener;

    public TwiOAuthWebHelper(Stage stage, OAuthWebHelperListener listener) {
        m_stage = stage;
        m_listener = listener;
    }

    public void auth(String title, String authUrl) {
        java.net.CookieHandler.setDefault(null);

        final TextField textField = new TextField();
        textField.setPromptText("Enter Auth Pin Code");
//        textField.setEditable(false);

        final Button button = new Button("Accept pin");
        button.setPrefSize(100, 20);
//        button.setDisable(true);

        Label label = new Label("Pin:");

        final WebView webView = new WebView();
        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State oldState, Worker.State newState) {
                String location = webView.getEngine().getLocation();
                if (newState == Worker.State.SUCCEEDED && location.contentEquals("https://api.twitter.com/oauth/authorize")) {
//                    button.setDisable(false);
//                    textField.setEditable(true);
                }
            }
        });

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                m_listener.authCodeReceived(textField.getText());
            }
        });

        webView.getEngine().load(authUrl);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #eee;");

        hbox.getChildren().addAll(label, textField, button);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(webView, hbox);

        Scene scene = new Scene(vbox, 800.0, 600.0);

        m_stage.setTitle(title);
        m_stage.setScene(scene);
        m_stage.show();
    }
}
