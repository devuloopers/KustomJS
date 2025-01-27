@JsModule("moment")
@JsNonModule
external fun moment(): MomentInstance

external interface MomentInstance {
    fun format(pattern: String): String
}