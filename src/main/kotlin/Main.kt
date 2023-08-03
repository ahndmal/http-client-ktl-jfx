import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.Background
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
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
        val mainBox = VBox()
        val mainPane = GridPane()
        val urlLabel = Label("URL")
        val urlField = TextField()
        urlField.padding = Insets(7.0)
        urlField.minWidth = 600.0

        val infoHeader = Label("")
        val responseSection = TextArea()

        val reqButton = Button("Send")
        reqButton.setOnMouseClicked { event ->
            if (urlField.length == 0) {
                infoHeader.text = "Error sending request"
                infoHeader.background = Background.fill(Paint.valueOf("red"))
                infoHeader.textFill = Paint.valueOf("white")
                infoHeader.font = Font.font(20.0)
                mainPane.add(infoHeader, 0, 0)
            } else {
                // successful response
                println(urlField.text)
                val client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1).build()

                val req = HttpRequest.newBuilder()
                    .GET().uri(URI.create(urlField.text)).build()

                val httpResponse = client.send(req, BodyHandlers.ofString())
                val statusCode = httpResponse.statusCode()
                val statusText = Text(String.format("Status code: %s", statusCode.toString()))
                statusText.style = "color: green"
                mainPane.add(statusText, 0, 4)

                val buttonsPane = GridPane()
                val menuButtons = HBox()

                val headersButton = Button("Headers")
                buttonsPane.add(headersButton, 1, 3)
                headersButton.padding = Insets(6.0)

                val statusButton = Button("Status")
                statusButton.padding = Insets(6.0)
                buttonsPane.add(statusButton, 1, 3)

                menuButtons.children.addAll(statusButton, headersButton)

//                val respHeaders = httpResponse.headers()
//                respHeaders.map().forEach {
//                    val headerText = Text(it.key)
//
//                }

                mainBox.children.addAll(menuButtons)

                responseSection.text = httpResponse.body()
                responseSection.padding = Insets(10.0)
                responseSection.prefWidth(600.0)
                responseSection.prefHeight(400.0)
                mainPane.add(responseSection, 0, 5)
            }
        }

        mainPane.add(urlLabel, 0, 1)
        mainPane.add(urlField, 0, 2)
        mainPane.add(reqButton, 1, 2)

        mainPane.padding = Insets(10.0)

        mainBox.children.add(mainPane)
        return mainBox
    }
}
