import org.apache.spark.sql.SparkSession

object Test4 {
  val s1 = "file:///root/text"
  val s2 = "file:////Users/gaoxin/WorkSpace/spark/article.xml"
  val s3 = "file:////Users/gaoxin/WorkSpace/spark/article_after.xml"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("XML_Test")
      .master("local[*]")
      .getOrCreate()

    val subNode = Array("article")


    //读取
    val file = spark.sparkContext.textFile(s2)

    //转换实体
    val afterParse = file.map(s => ReplaceEntity.parse(s)).cache()

    afterParse.foreach(it => println(it))


    afterParse.saveAsTextFile(s3)
  }
}