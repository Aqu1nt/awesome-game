package ch.awesome.game.client.rendering.loading.xml

object XMLLoader {

    private val DATA = Regex(">(.+?)<")
    private val START_TAG = Regex("<(.+?)>")
    private val END_TAG = Regex("")
    private val ATTRIBUTE_NAME = Regex("")
    private val ATTRIBUTE_VALUE = Regex("")

//    fun convertToXMLNode(gl: WebGL2RenderingContext, data: String): XMLNode {
//        XMLNode node = XMLNode()
//    }

//    private fun setData(node: XMLNode, line: String) {
//        if(DATA.containsMatchIn(line)) {
//            val matcher = DATA.find(line)
//            node.data = line.
//        }
//    }
}