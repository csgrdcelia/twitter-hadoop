import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Main {
  def main(args: Array[String]): Unit = {
    val sparkSession : SparkSession = SparkSession
      .builder()
      .master("local")
      .appName("tweet_analysis")
      .getOrCreate()
//    val current_dir = new java.io.File(".").getCanonicalPath
    val path = "D:\\ESGI\\5AL-S2\\spark_hadoop\\twitter-hadoop\\tweet_receiver\\results\\tweets\\*"
    println(path)
    val rawDF = sparkSession.read.json(path)
//    print(rawDF.schema)
    val schema = rawDF.schema
    rawDF.show()
//    println(rawDF.count)
    val hashtagDF = rawDF
      .filter(size(col("HashtagEntities")) > 0)
      .select(col("createdAt"), explode(col("HashtagEntities")))
      .select(col("createdAt"), col("col").getItem("text").name("hashtag"))

    hashtagDF.show()

    val countedHashtags = hashtagDF
      .groupBy("hashtag")
      .count()
      .orderBy(desc("count"))
    countedHashtags.show
  }
}
