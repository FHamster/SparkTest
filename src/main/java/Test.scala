import org.apache.spark.sql.SparkSession
import com.databricks.spark.xml

object Test {
  def main(args: Array[String]): Unit = {


    val spark = SparkSession
      .builder
      .appName("XML_Test")
      .master("local[8]")
//      .config("driver-memory", "4096M")
      .config("spark.executor.memory", "4G")
      .getOrCreate()

    val df = spark
      .read
      .format("com.databricks.spark.xml")
      .option("rowTag", "dblp")
//      .load("a.xml")
      .load("dblp.xml")
    //    val df = spark.read.option("rowTag", "dblp").load("dblp.xml")

    //    val selectedData = df.select("author", "_id")

//    df.show()
    df.printSchema()
  }
}
