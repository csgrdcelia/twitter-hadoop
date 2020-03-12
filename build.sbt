name := "twitter-hadoop"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "1.6.2"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "1.6.2"
libraryDependencies += "org.apache.spark" % "spark-streaming-twitter_2.11" % "1.6.2"