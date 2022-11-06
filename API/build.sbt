ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

val libVersion =
  new {
    val javaSpark      = "2.9.4"
    val logback        = "1.2.11"
    val circe          = "0.14.2"
    val testcontainers = "1.17.3"
    val cassandra      = "4.14.1"
  }

lazy val root = (project in file("."))
  .settings(
    name := "API",
    libraryDependencies ++= Seq(
      "com.sparkjava" % "spark-core"         % libVersion.javaSpark,
      "io.circe"      %% "circe-parser" % libVersion.circe,
      "ch.qos.logback"   % "logback-classic" % libVersion.logback,
      "org.testcontainers"            % "testcontainers"            % libVersion.testcontainers,
      "org.testcontainers"            % "cassandra"                 % libVersion.testcontainers,
      "com.datastax.oss"              % "java-driver-core"          % libVersion.cassandra,
      "com.datastax.oss"              % "java-driver-query-builder" % libVersion.cassandra,
    )
  )
