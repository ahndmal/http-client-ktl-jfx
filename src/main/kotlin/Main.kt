import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.jsoup.Jsoup
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

class Main : Application() {
    override fun start(primaryStage: Stage?) {
        val scene = Scene(createContent(), 1200.0, 900.0)
        primaryStage?.title = "HTTP Client"
        primaryStage?.scene = scene
        primaryStage?.show()
    }

    fun createContent(): Parent {
        val mainPane = GridPane()
        val urlLabel = Label("URL")
        val urlField = TextField()
        urlField.padding = Insets(7.0)

        val responseSection = TextArea()

        val reqButton = Button("Send")
        reqButton.setOnMouseClicked { event ->
            if (urlField.length == 0) {
                //todo
            } else {
                println(urlField.text)
                val client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1).build()

                val req = HttpRequest.newBuilder()
                    .GET().uri(URI.create(urlField.text)).build()

                val httpResponse = client.send(req, BodyHandlers.ofString())

                val doc = Jsoup.parse(httpResponse.body())

                responseSection.text = doc.body().text()
                responseSection.padding = Insets(10.0)
                mainPane.add(responseSection, 0, 3)
            }
        }

        mainPane.add(urlLabel, 0, 0)
        mainPane.add(urlField, 0, 1)
        mainPane.add(reqButton, 1, 1)
        mainPane.padding = Insets(10.0)

        val mainBox = VBox(mainPane)
        return mainBox
    }
}
