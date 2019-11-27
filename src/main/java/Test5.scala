import org.apache.spark.sql.SparkSession

object Test5 {
  val s1 = "file:///root/dblp.xml"
  val s2 = "file:///root/dblp_after.xml"
  val s3 = "file:////Users/gaoxin/WorkSpace/spark/article_after.xml"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("XML_Test").master("local[*]").getOrCreate()


    val df = spark.read.format("com.databricks.spark.xml").option("rootTag", "dblp").option("rowTag", "article").load(s2)

    df.show()
  }
}
