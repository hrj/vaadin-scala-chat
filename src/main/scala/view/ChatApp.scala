package view

import model._

import com.vaadin.Application
import com.vaadin.service.ApplicationContext
import vaadin.scala._
import com.vaadin.terminal.{Sizeable, ExternalResource, ThemeResource}
import com.vaadin.ui.{Alignment, PasswordField}
import com.vaadin.event.{ItemClickEvent, ShortcutListener, ShortcutAction}
import com.github.wolfie.refresher._

object ChatData {
  lazy val dataSource = MessageRooms.createRoom
  var regUsers = Map[String,String]()
}

class ChatWindow(app:ChatApp) extends Window("chat") {
  import ChatData._
  
  private val tree = new NavigationTree(event => treeItemSelected(event))
  private val horizontalSplit = new HorizontalSplitPanel

  private val roomView = new VerticalLayout (100 pct) {
    val msgView = add(new MsgView(dataSource), ratio = 1)

    val addMsgView = add(new HorizontalLayout(100 pct) {

      private val msgField = new TextField(width = 100 pct) {
        addShortcutListener(new helper.EnterListener {
          override def handleAction(sender:Any, target:Any) = sendMsgAndClear
        })
        focus
      }

      val newMsg:TextField = add(msgField, ratio = 1, alignment = Alignment.MIDDLE_LEFT)
      val sendButton = add(new Button("Send", _ => sendMsgAndClear))

      private def sendMsgAndClear = {
        val msg = newMsg.getValue.toString
        app.addMessage(msg)
        scrollToLast
        newMsg.setValue("")
      }
    })
  }

  private def scrollToLast {
    roomView.msgView.setCurrentPageFirstItemId(dataSource.lastItemId)
  }

  horizontalSplit.setSplitPosition(150, Sizeable.UNITS_PIXELS)
  horizontalSplit.setFirstComponent(tree)
  horizontalSplit.setSecondComponent(roomView)

  val refresherComponent = new Refresher
  refresherComponent.setRefreshInterval(1000)
  refresherComponent.addListener(new Refresher.RefreshListener {
    def refresh(source:Refresher) { scrollToLast }
  })


  setContent(new VerticalLayout(100 pct, 100 pct) {
    add(refresherComponent)
    add(createToolbar)
    add(horizontalSplit, ratio = 1)
  })

  setTheme("contacts")
  scrollToLast


  private def createToolbar = {
    new HorizontalLayout(width = 100 pct,spacing = true, style = "toolbar") {
      val help = add(new Button("Help"))
      help.setIcon(new ThemeResource("icons/24/help.png"))
      add(new Embedded(source = new ThemeResource("images/logo.png")), ratio = 1, alignment = Alignment.MIDDLE_RIGHT)
    }
  }
  
  private def treeItemSelected(event: ItemClickEvent): Unit = event.getItemId match {
      case NavigationTree.ROOMS => {
        dataSource.removeAllContainerFilters
      }
  }
}

object ChatApp {
  val s = new ThreadLocal[ChatApp]
  
  def getInstance = s.get
}

class ChatApp extends Application with ApplicationContext.TransactionListener {
  import ChatData._
  def init = {
    setMainWindow(new LoginWindow)
    getContext.addTransactionListener(this)
  }

  def transactionStart(application:Application, o:Any):Unit = application match {
    case s:ChatApp => ChatApp.s.set(s)
    case _ => 
  }

  def transactionEnd(application:Application, o:Any) = application match {
    case s:ChatApp => ChatApp.s.set(null); ChatApp.s.remove
    case _ => 
  }

  private var loggedInUser:Option[String] = None

  def authenticate(login:String, password:String) = {

    val regPassword = regUsers.get(login)
    if (regPassword.isDefined) {
     if (regPassword.get equals password) {
      loggedInUser = Some(login)
      addMessage(MessageRooms.SYS_USER_NAME, "Welcome " + login)
      loadProtectedResources
     } else {
      throw new Exception("Wrong password")
     }
    } else {
      regUsers += (login -> password)
      loggedInUser = Some(login)
      addMessage(MessageRooms.SYS_USER_NAME, "A hearty welcome to our new friend: " + login)
      loadProtectedResources
    }
  }

  def addMessage(msg:String):Unit = addMessage(loggedInUser.get, msg)

  def addMessage(user:String, msg:String):Unit = {
    dataSource.addItem(new Message(user, msg, new java.util.Date))
  }

  private def loadProtectedResources = {
    setMainWindow(new ChatWindow(this))
  }
}

object NavigationTree {
  val ROOMS = "Rooms"
}

class NavigationTree(itemClickAction: ItemClickEvent => Unit) 
    extends Tree(selectable = true, nullSelectionAllowed = false) {
  addItemClickListener(itemClickAction)
  
  addItem(NavigationTree.ROOMS)
  setChildrenAllowed(NavigationTree.ROOMS, false)
}

class LoginWindow extends Window("authentication required") {
  add ( new Label ("Please login in order to use the application") )

  private val login = add(new TextField ( "Username"))
  private val password = add(new PasswordField ( "Password") {
    addShortcutListener(new helper.EnterListener {
      override def handleAction(sender:Any, target:Any) = submitPassword
    })
  })

  private val btnLogin = add(new Button("Login", _ => submitPassword))

  private def submitPassword {
    try {
        val instance = ChatApp.getInstance
        instance.authenticate(login.getValue.toString, password.getValue.toString)
        open(new ExternalResource (instance.getURL))
    } catch {
      case e:Exception => showNotification(e.toString)
    }
  }

  setName ("login")
  login.focus

}
