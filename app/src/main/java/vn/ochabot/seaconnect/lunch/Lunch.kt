package vn.ochabot.seaconnect.lunch


/**
 * @author linhtruong
 */
class Lunch(
        var id: String = "",
        var title: String = "",
        var count: String = "",
        var img: String = "",
        var source: String = "",
        var des: String = "",
        var ref: String = ""
) {
    companion object {
        val DB = "foods"
        val SOURCE = "source"
        val DES = "des"
        val ID = "meal_id"
    }
}