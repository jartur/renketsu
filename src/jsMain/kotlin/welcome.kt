import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.xhr.XMLHttpRequest
import react.*
import react.dom.div
import react.dom.input

external interface WelcomeProps : RProps {
    var name: String
}

data class WelcomeState(val name: String, var lastResponse: String = "nope") : RState

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
                        setState { lastResponse = rq.responseText }
                    }
                    rq.open("GET", "/db/${state.name}")
                    rq.send()
                }
            }
        }
        div {
            nodeChild = child(Node::class) {
                attrs {
                    name = state.lastResponse
                }
            }
        }
    }
}
