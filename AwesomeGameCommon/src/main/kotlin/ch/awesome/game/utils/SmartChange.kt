package ch.awesome.game.utils


interface ISmartChange {
    val id: String
    val value: Any?
    val type: SmartChangeType
}

data class SmartChange(
        override val id: String,
        override val value: Any?,
        override val type: SmartChangeType): ISmartChange