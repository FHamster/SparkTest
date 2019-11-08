import org.apache.spark.sql.SparkSession

object Test2 {
  def main(args: Array[String]): Unit = {


    val spark = SparkSession
      .builder
      .appName("XML_Test")
      .master("local[*]")
      .getOrCreate()

    val subNode = Array("article", "inproceedings", "proceedings", "book",
      "incollection", "phdthesis", "mastersthesis", "www", "person", "data")

    val subNodeRowNum= subNode.map(it => {
      val rowNum: Long = spark.read
        .format("com.databricks.spark.xml")
        .option("rootTag", "dblp")
        .option("rowTag", it)
        .load("file:///root/dblp.xml").count()
      (it, rowNum)})
      .array

    subNodeRowNum.foreach(it=> println(it))
  }
}
