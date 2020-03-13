import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Main {
  def main(args: Array[String]): Unit = {
    val sparkSession : SparkSession = SparkSession
      .builder()
      .master("local")
      .appName("tweet_analysis")
      .getOrCreate()

    val path = "results/tweets/"
    val rawDF = sparkSession.read.json(path)
    val schema = rawDF.schema
    rawDF.show()

    val currentHourTimestamp = unix_timestamp(current_timestamp()).multiply(1000)
    val previousHourTimestamp = currentHourTimestamp.divide(1000).minus(60 * 60).multiply(1000)

    val hashtagDF = rawDF
      .filter(size(col("HashtagEntities")) > 0)
      .select(col("time"), explode(col("HashtagEntities")))
      .select(col("time"), col("col").getItem("text").name("hashtag"))
      .filter(col("time").between(previousHourTimestamp, currentHourTimestamp))

    val countedHashtags = hashtagDF
      .groupBy("hashtag")
      .count()
      .orderBy(desc("count"))
    countedHashtags.show

    val currentTime = sparkSession.range(1).select(currentHourTimestamp).collectAsList().get(0).get(0)
    countedHashtags.repartition(1).write.json("results/trends/time=" + currentTime)
  }
}
