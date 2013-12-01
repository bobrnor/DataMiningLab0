package crawlers.auths;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 12/1/13
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
public class OAuthWebHelper {
    private Stage m_stage;
    private OAuthWebHelperListener m_listener;

    public OAuthWebHelper(Stage stage, OAuthWebHelperListener listener) {
        m_stage = stage;
        m_listener = listener;
    }

    public void auth(String title, String authUrl) {
        WebView webView = new WebView();
        webView.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> observableValue, Document oldDocument, Document newDocument) {
                if (newDocument != null) {
                    URL url = null;
                    try {
                        url = new URL(newDocument.getDocumentURI());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    if (url != null && url.getRef() != null) {
                        String[] strings = url.getRef().split("=");
                        m_listener.authCodeRecieved(strings[1]);
                    }
                }
            }
        });

        webView.getEngine().load(authUrl);

        Scene scene = new Scene(webView, 640.0, 480.0);
        m_stage.setTitle(title);
        m_stage.setScene(scene);
        m_stage.show();
    }
}
