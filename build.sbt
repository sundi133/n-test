name := "n_test"

version := "1.0"

//scalaVersion := "2.11.8"

scalaVersion := "2.11.6"

val sparkVersion = "1.6.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-twitter" % sparkVersion
)

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "3.0.3",
  "org.twitter4j" % "twitter4j-stream" % "3.0.3"
)

libraryDependencies += "com.google.code.gson" % "gson" % "1.7.1"
libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.6"
libraryDependencies += "org.spire-math" %% "spire" % "0.11.0"
// https://mvnrepository.com/artifact/org.apache.spark/spark-sql_2.10