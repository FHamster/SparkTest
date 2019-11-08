import org.apache.spark.sql.SparkSession

object Testa {
  def main(args: Array[String]): Unit = {


    val spark = SparkSession
      .builder
      .appName("XML_Test")
      .master("local[2]")
      .config("spark.ui.enabled", value = true)
      .getOrCreate()


    val df = spark
      .read
      .format("com.databricks.spark.xml")
      .option("rowTag", "dblp")
      //      .load("a.xml")
      .load("a.xml")
    //    val df = spark.read.option("rowTag", "dblp").load("dblp.xml")

    //    val selectedData = df.select("author", "_id")

    //    df.show()
    df.printSchema()
    val result = df.select("article.author").collect()
    result.foreach(x => println(x))
    spark.stop()
  }
}
