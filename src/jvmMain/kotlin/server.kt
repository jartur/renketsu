import com.arangodb.ArangoDB
import com.arangodb.entity.BaseDocument
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.jackson.*
import io.ktor.response.*
import kotlinx.html.*

fun HTML.index() {
    head {
        title("Hello from Ktor!")
        style { unsafe { raw(".node { border: black solid }") } }
    }
    body {
        div {
            +"Hello from Ktor"
        }
        div {
            id = "root"
        }
        script(src = "/static/output.js") {}
    }
}

val arangoDb = ArangoDB.Builder().build()

data class CollectionInfo(val info: BaseDocument?)


fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(ContentNegotiation) {
            jackson()
        }
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            get("/db/{collection}/{document}") {
                println("Incoming get")
                val coll = arangoDb.db("renketsu").collection(call.parameters["collection"])
                call.respond(CollectionInfo(coll.getDocument(call.parameters["document"], BaseDocument::class.java)))
            }
            static("/static") {
                resources()
            }
        }
    }.start(wait = true)
}