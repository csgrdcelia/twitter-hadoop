package com.Ingeniance.spark
import java.io.FileNotFoundException
import java.util.Properties
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._

import scala.io.Source


object Main extends App {
  val url = getClass.getClassLoader.getResource("application.properties")
  val properties: Properties = new Properties()

  if (url != null) {
    val source = Source.fromURL(url)
    properties.load(source.bufferedReader())
  }

  System.setProperty("twitter4j.oauth.consumerKey", properties.getProperty("consumerKey"))
  System.setProperty("twitter4j.oauth.consumerSecret", properties.getProperty("consumerSecret"))
  System.setProperty("twitter4j.oauth.accessToken", properties.getProperty("accessToken"))
  System.setProperty("twitter4j.oauth.accessTokenSecret", properties.getProperty("accessTokenSecret"))

  val ssc = new StreamingContext("local[*]", "PrintTweets", Seconds(1))

  val filters = Array("COVID19")
  val tweets = TwitterUtils.createStream(ssc, None, filters)

  val statuses = tweets.map(status => status.getText())
  statuses.print()

  val englishTweets = tweets.filter(_.getLang() == "en")

  englishTweets.foreachRDD{ (x, time) =>
    if(!x.isEmpty){
      x.saveAsTextFile("results/tweets/file" + time )
    }
  }

  ssc.start()
  ssc.awaitTermination()

}