package ch.awesome.game.client.rendering.loading.xml

class XMLNode(val name: String) {

    var data: String = ""
    val attributes = HashMap<String, String>()
    val children = HashMap<String, MutableList<XMLNode>>()

    fun addAttribute(name: String, value: String) {
        attributes[name] = value
    }

    fun getAttributeValue(name: String): String {
        return attributes[name] ?: throw IllegalStateException("no attribute with name $name")
    }

    fun addChild(child: XMLNode) {
        val list = children[child.name] ?: mutableListOf()

        if (list.isEmpty()) {
            children[child.name] = list
        }

        list.add(child)
    }

    fun getChildren(name: String): MutableList<XMLNode> {
        return children[name] ?: throw IllegalStateException("no child with name $name available!")
    }

    fun getChildWithAttribute(name: String, attribute: String, value: String): XMLNode {
        val children = getChildren(name)

        for(c in children) {
            val childValue = c.getAttributeValue(attribute)
            if (value == childValue) {
                return c
            }
        }

        throw IllegalStateException("no child with name $name, attribute $attribute and value $value")
    }
}