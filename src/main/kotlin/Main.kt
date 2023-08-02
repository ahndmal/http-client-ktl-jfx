
import javafx.application.Application
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage

class Main : Application() {
    override fun start(primaryStage: Stage?) {
        val scene = Scene(createContent(), 1024.0, 768.0)
        primaryStage?.title = "HTTP Client"
        primaryStage?.scene = scene
        primaryStage?.show()
    }

    fun createContent(): Parent {
        val mainPane = GridPane()
        val mainBox = VBox(mainPane)
        return mainBox
    }
}
