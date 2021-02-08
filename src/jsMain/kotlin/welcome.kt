import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.xhr.XMLHttpRequest
import org.w3c.xhr.XMLHttpRequestEventTarget
import react.*
import react.dom.div
import react.dom.input
import kotlin.js.Json

external interface WelcomeProps : RProps {
    var name: String
}

data class WelcomeState(val name: String, var lastResponse: Json? = null) : RState

@JsExport
class Welcome(props: WelcomeProps) : RComponent<WelcomeProps, WelcomeState>(props) {

    init {
        state = WelcomeState(props.name)
    }

    lateinit var nodeChild: ReactElement

    override fun RBuilder.render() {
        div {
            +"Hello, ${state.name}: ${state.lastResponse}"
        }
        input {
            attrs {
                type = InputType.text
                value = state.name
                onChangeFunction = { event ->
                    setState(
                        WelcomeState(name = (event.target as HTMLInputElement).value)
                    )
                }
            }
        }
        input {
            attrs {
                type = InputType.button
                value = "Fuck me hard"
                onClickFunction = { e ->
                    val rq = XMLHttpRequest()
                    rq.onreadystatechange = { e ->
                        if (rq.readyState.toInt() == 4) {
                            setState {
                                lastResponse = JSON.parse<Json>(rq.responseText).get("info") as Json
                            }
                        }
                    }
                    rq.open("GET", "/db/${state.name}")
                    rq.send()
                }
            }
        }
        div {
            nodeChild = child(Node::class) {
                attrs {
                    name = state.lastResponse?.get("key") as? String
                    document = state.lastResponse
                }
            }
        }
    }
}