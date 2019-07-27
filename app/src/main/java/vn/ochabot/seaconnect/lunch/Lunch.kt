package vn.ochabot.seaconnect.lunch


/**
 * @author linhtruong
 */
class Lunch(
        var id: String = "",
        var title: String = "",
        var count: String = "",
        var url: String = "",
        var source: String = "",
        var des: String = "",
        var ref: String = ""
) {
    companion object {
        val DB = "foods"
        val SOURCE = "source"
        val DES = "des"
        val ID = "meal_id"

        fun copy(item: Lunch): Lunch {
            var newItem = Lunch()
            newItem.id = item.id
            newItem.title = item.title
            newItem.count = item.count
            newItem.url = item.url
            newItem.source = item.source
            newItem.des = item.des
            newItem.ref = item.ref

            return newItem
        }
    }
}