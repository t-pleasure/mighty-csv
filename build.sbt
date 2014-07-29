name := "mighty-csv"

version := "0.2"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.9.3", "2.9.2", "2.9.1")

retrieveManaged := true

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) 
    Some("snapshots" at nexus + "content/repositories/snapshots") 
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/t-pleasure/mighty-csv</url>
  <licenses>
    <license>
      <name>Apache 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:t-pleasure/mighty-csv.git</url>
    <connection>scm:git:git@github.com:t-pleasure/mighty-csv.git</connection>
  </scm>
  <developers>
    <developer>
      <id>t-pleasure</id>
      <name>Tony Tran</name>
      <url>http://github.com/t-pleasure</url>
    </developer>
  </developers>)


libraryDependencies += "net.sf.opencsv"%"opencsv"%"2.3"

libraryDependencies += "junit"%"junit"%"4.8.2"%"test"

libraryDependencies += PartialFunction.condOpt(CrossVersion.partialVersion(scalaVersion.value)){
  case Some((2, scalaMajor)) if scalaMajor >= 10 =>
    "org.scalatest" %% "scalatest" % "2.2.0" % "test"
  case Some((2, scalaMajor)) if scalaMajor == 9 =>
    "org.scalatest" %% "scalatest" % "1.9.2" % "test"
}.get

