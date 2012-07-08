package model

import java.io.Serializable
import scala.reflect.BeanProperty


class Message(
     @BeanProperty var userName: String = "",
     @BeanProperty var msg: String = "",
     @BeanProperty var timeStamp: java.util.Date = null
) extends Serializable 

import com.vaadin.data.util.BeanItemContainer

class MessageRoom extends BeanItemContainer[Message](classOf[Message]) with Serializable

object MessageRooms  {
  val SYS_USER_NAME = "sys"

  /** Natural property order for Person bean. Used in tables and forms.  */
  val NATURAL_COL_ORDER:Array[Object] = Array(
    "userName", "msg"
  )

  /** "Human readable" captions for properties in same order as in NATURAL_COL_ORDER.  */
  val COL_HEADERS_ENGLISH = Array(
    "Name", "Message"
  )
  
  def createRoom = {
    println("Creating new room")
    val room = new MessageRoom

    room.addItem(new Message(SYS_USER_NAME, "Server started", new java.util.Date))
    room
  }
}
