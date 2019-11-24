import org.apache.spark.sql.SparkSession

object Test3 {
  def main(args: Array[String]): Unit = {
    val sc= SparkSession
      .builder
      .appName("XML_Test")
      .master("local[*]")
      .getOrCreate()

  }
}
