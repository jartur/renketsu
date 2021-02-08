import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.xhr.XMLHttpRequest
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.input

external interface NodeProps : RProps {
    var name: String?
    var document: Any?
}

data class NodeState(val name: String?) : RState

@JsExport
class Node(props: NodeProps) : RComponent<NodeProps, NodeState>(props) {

    init {
        state = NodeState(props.name)
    }

    override fun RBuilder.render() {
        div("nodeHeader") {
            +(props.name ?: "N/A")
        }
        div("nodeBody") {
            +JSON.stringify(props.document)
        }
    }
}
