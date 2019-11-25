import org.apache.spark.sql.SparkSession

object Test4 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("XML_Test")
      .master("local[*]")
      .getOrCreate()

    val subNode = Array("article")


    val df = spark.read
      .text("file:///root/text")

    df.foreach(row => println(row)
    )


  }
}