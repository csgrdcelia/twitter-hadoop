import java.util.Properties

import com.google.gson.Gson
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._

import scala.io.Source

object Main extends App {
  override def main(args: Array[String]): Unit = {
    if(args.length == 0) {
      println("Missing argument : Time between tweets recuperation in seconds.")
      return
    }

    val seconds = Integer.valueOf(args.apply(0)).toLong
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

    val streamingContext = new StreamingContext("local[*]", "PrintTweets", Seconds(seconds))
    val tweets = TwitterUtils.createStream(streamingContext, None)
    val englishTweets = tweets.filter(_.getLang() == "en").map(new Gson().toJson(_))

    englishTweets.foreachRDD(rdd => println(rdd))

    englishTweets.foreachRDD { (x, time) =>
      if (!x.isEmpty) {
        x.repartition(1).saveAsTextFile("results/tweets/time=" + time.milliseconds)
      }
    }

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
