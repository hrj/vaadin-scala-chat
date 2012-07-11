name := "scala-chat"
 
scalaVersion := "2.9.2"
 
seq(gwtSettings: _*)

gwtVersion := "2.4.0"

seq(webSettings: _*)

resolvers += "Vaadin add-ons repository" at "http://maven.vaadin.com/vaadin-addons"

libraryDependencies ++= Seq(
  "com.vaadin" % "vaadin" % "6.8.0",
  "org.vaadin.addons" % "scaladin" % "1.0.0",
  // "org.vaadin.addons" % "refresher" % "1.1.1",
  "org.vaadin" % "dontpush-addon-ozonelayer" % "0.4.8",
  "ch.qos.logback" % "logback-classic" % "1.0.6"
)

libraryDependencies ++= Settings.jettyContainer

classpathTypes ~= (_ + "orbit")

// hack: sbt-gwt-plugin assumes that sources are in src/main/java
// javaSource in Compile <<= (scalaSource in Compile)

gwtModules := List("helper.ChatWidgetset")

// more correct place would be to compile widgetset under the target dir and configure jetty to find it from there
gwtTemporaryPath := file(".") / "src" / "main" / "webapp" / "VAADIN" / "widgetsets"

// hack
// ivyXML := 
  // <dependency org="org.vaadin" name="dontpush-addon-ozonelayer" rev="0.4.8">
    // <exclude org="org.atmosphere" module="atmosphere-compat-jetty"/>
  // </dependency>
