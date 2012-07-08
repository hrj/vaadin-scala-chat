package view

import model._
import vaadin.scala._

class MsgView(dataSource: MessageRoom)
  extends Table(width = 100 pct, height = 100 pct, dataSource = dataSource) {

  setVisibleColumns(MessageRooms.NATURAL_COL_ORDER)
  setColumnHeaders(MessageRooms.COL_HEADERS_ENGLISH)

  addGeneratedColumn("time", (table, itemID, propertyId) => itemID match {
      case m: Message => new Label(timeFormat(m.timeStamp))
  })

  setColumnExpandRatio(MessageRooms.NATURAL_COL_ORDER(1), .7f)
  setColumnExpandRatio("time", .3f)

  setColumnCollapsingAllowed(true)
  setColumnReorderingAllowed(true)

  setSelectable(true)
  setImmediate(true)
  setNullSelectionAllowed(false)

  private def timeFormat(t1:java.util.Date) = {
    val t1ms = t1.getTime
    val t2ms = (new java.util.Date).getTime
    val seconds = (t2ms - t1ms)/1000
    if (seconds < 60) {
      "few moments ago"
    } else {
      val mins = seconds / 60
      if (mins < 60) {
        "%d mins ago" format (mins)
      } else {
        val hours = mins / 60

        "%d hours ago" format (hours)
      }
    }
  }
}

