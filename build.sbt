name := "AppStoreMini"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.26",
  "com.google.guava" % "guava" % "14.0",
  "com.typesafe" %% "play-plugins-redis" % "2.1.1",
  "com.github.ddth" %% "play-module-plommon" % "0.3.2-SNAPSHOT",
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings
