import sbt._

object Settings {

  private val jetty = "new"

  private val servletJar = Artifact("javax.servlet", "jar", "jar")

  val jettyContainer = 
    if (jetty equals "mort") {
      val jettyVersion = "6.1.20"

      List(
        "org.mortbay.jetty" % "jetty" % jettyVersion % "container"
      )
    } else if (jetty equals "old") {
      val jettyVersion = "7.6.4.v20120524"

      List(
        "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "container",
        ("org.eclipse.jetty.orbit" % "javax.servlet" % "2.5.0.v201103041518" artifacts servletJar) % "container",
        "org.eclipse.jetty" % "jetty-websocket" % jettyVersion
      )
    } else if (jetty equals "new") {
      val jettyVersion = "8.0.4.v20111024"

      List(
        "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "container"
        // "org.eclipse.jetty" % "jetty-websocket" % jettyVersion
      )

    } else {
      val jettyVersion = "8.1.4.v20120524"
      List(
        "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "container",
        ("org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" artifacts servletJar) % "container",
        "org.eclipse.jetty" % "jetty-websocket" % jettyVersion
      )

    }

  val tomCatContainer = List(
    "org.apache.tomcat.maven" % "tomcat7-maven-plugin" % "2.0-beta-1"
   )
}

