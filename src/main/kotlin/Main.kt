import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

class Main : Application() {
    override fun start(primaryStage: Stage?) {
        val scene = Scene(createContent(), 1200.0, 900.0)
        primaryStage?.title = "HTTP Client"
        primaryStage?.scene = scene
        primaryStage?.show()
    }

    private fun createContent(): Parent {
        val mainBox = VBox()
        mainBox.padding = Insets(10.0)

        val requestBox = VBox()
        requestBox.padding = Insets(10.0)
        requestBox.border = Border.stroke(Paint.valueOf("orange"))
        HBox.setMargin(requestBox, Insets(10.0))

        val reqPane = GridPane()

        val reqHeaderButton = Button("Headers")

        // todo
        reqHeaderButton.setOnMouseClicked {evt ->
            val btn = evt.target
            // todo
        }

        val methodType: ChoiceBox<Pair<String, String>> = ChoiceBox()
        methodType.items.addAll(
            Pair("GET", "GET"),
            Pair("POST", "POST"),
            Pair("PUT", "PUT"),
            Pair("DELETE", "DELETE"),
        )
        methodType.maxWidth = 100.0
        methodType.value = Pair("GET", "GET")

        val reqBody = TextArea()
        reqBody.minWidth = 500.0
        reqBody.minHeight = 300.0

        // response
        val responseBox = VBox()
        responseBox.padding = Insets(10.0)
        responseBox.border = Border.stroke(Paint.valueOf("green"))
        HBox.setMargin(responseBox, Insets(10.0))

        val responsePane = GridPane()
        responsePane.padding = Insets(10.0)

        val urlLabel = Label("URL")
        val urlField = TextField()
        urlField.padding = Insets(7.0)
        urlField.minWidth = 700.0

        val infoHeader = Label("")
        val responseSection = TextArea()
        responseSection.minWidth = 700.0
//        responseSection.style = "width: 700px"

        val reqButton = Button("Send")
        reqButton.background = Background.fill(Paint.valueOf("orange"))
        reqButton.setOnMouseClicked { event ->
            if (urlField.length == 0) {
                infoHeader.text = "Error sending request"
                infoHeader.background = Background.fill(Paint.valueOf("red"))
                infoHeader.textFill = Paint.valueOf("white")
                infoHeader.font = Font.font(20.0)
                responsePane.add(infoHeader, 0, 0)
            } else {
                // correct request data
                responseBox.children.removeAll()


                // HTTP client
                val client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1).build()

                val req = HttpRequest.newBuilder()
                    .uri(URI.create(urlField.text))

                when(methodType.value.second) {
                    "GET" -> req.GET()
                    "POST" -> req.POST(BodyPublishers.ofString(reqBody.text))
                    "PUT" -> req.PUT(BodyPublishers.ofString(reqBody.text))
                    "DELETE" -> req.DELETE()
                }

                // response
                val httpResponse = client.send(req.build(), BodyHandlers.ofString())
                val statusCode = httpResponse.statusCode()
                val statusText = Text(String.format("Status code: %s", statusCode.toString()))
                statusText.selectionFill = Paint.valueOf("green")

                responsePane.add(statusText, 0, 4)

                val buttonsPane = GridPane()
                val respMenuButtons = HBox()

                val headersButton = Button("Headers")
                buttonsPane.add(headersButton, 1, 3)
                headersButton.padding = Insets(6.0)
                headersButton.background = Background.fill(Paint.valueOf("orange"))

                val statusButton = Button("Status")
                statusButton.padding = Insets(6.0)
                statusButton.background = Background.fill(Paint.valueOf("orange"))

                buttonsPane.add(statusButton, 1, 3)

                respMenuButtons.children.addAll(statusButton, headersButton)

//                val respHeaders = httpResponse.headers()
//                respHeaders.map().forEach {
//                    val headerText = Text(it.key)
//
//                }

                responseBox.children.add(0, respMenuButtons)

                responseSection.text = httpResponse.body()
                responseSection.padding = Insets(10.0)
                responseSection.minWidth(600.0)
                responseSection.prefHeight(400.0)
                responsePane.add(responseSection, 0, 5)
            }
        }


//        reqPane.add(urlLabel, 0, 1)
        reqPane.add(methodType, 0, 2)
        reqPane.add(urlField, 1, 2)
        reqPane.add(reqButton, 2, 2)
        reqPane.add(reqBody, 1, 3)

        requestBox.children.add(reqPane)

        responseBox.children.add(responsePane)

        mainBox.children.addAll(
            requestBox, responseBox
        )

        return mainBox
    }
}
