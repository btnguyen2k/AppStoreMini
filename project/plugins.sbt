// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Sonatype snapshots repository" at "http://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "pk11-scratch" at "http://pk11-scratch.googlecode.com/svn/trunk"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.0")