import java.io.{BufferedOutputStream, File, FileOutputStream, OutputStreamWriter}

import org.apache.spark._
import org.apache.spark.rdd.RDD

/**
 * TaxiCount测试
 * 从test.txt读取数据
 */
object TaxiCount {
  def main(args: Array[String]): Unit = {
    println("开始运行")

    val conf: SparkConf = new SparkConf()
      .setAppName("WordCount")
      .setMaster("local[4]") //设置为本地4核处理

    //    conf.getAll.foreach(x => println(x))
    val sc: SparkContext = new SparkContext(conf)

    //表结构为
    //出租车ID、时间、经度、维度、夹角角度、出租车的瞬时速度和出租车载客状态
    //读取全部出租车记录
//    val input: RDD[String] = sc.textFile("./Taxi_070220/Taxi_105")
    //读取105号车
        val input: RDD[String] = sc.textFile("./Taxi_*")

    //计算数据记录数量
    val n = input.count()
    println(n)

    //去除重复点的出租车数据
    //    val distinTaxiRecRDD: RDD[((String, String, String), Array[String])] = input
    val distinTaxiRecRDD: RDD[Array[String]] = input
      //    val distinTaxiRecRDD = input
      .map(line => line.split(','))
      .groupBy(tu => (tu(0), tu(2), tu(3))) //去除重复数据
      .map(tu => tu._2.head)
      .cache() //进行内存缓存

    println("有效记录个数：" + distinTaxiRecRDD.count())

    //浦东机场经纬度
    //北侧 31.1695551080 121.7824172974
    //南侧 31.1024805571 121.8449020386
    //中部 31.1360178330 121.8136596680

    //点差平方 0.001124749 0.000976086
    //半径 0.045834864

    //机场中心的北纬坐标
    val centerN: Double = 31.1360178330
    //机场中心的的东经坐标
    val centerE: Double = 121.8136596680
    //机场范围内的点的点差平方和最大值
    val SumPointDev: Double = 0.001124749 + 0.000976086

//    31.1360178330
    val taxiRecInAirport = distinTaxiRecRDD.filter(arr => {
      var n: Double = arr(3).toDouble
      var e: Double = arr(2).toDouble
      (n - centerN) * (n - centerN) + (e - centerE) * (e - centerE) <= SumPointDev
    }).cache()

    val taxiInUse = taxiRecInAirport
      .filter(tu => tu(6).toInt == 1)
      .cache()

    println("机场范围内的载客记录数量:" + taxiInUse.count())

    val taxiNotUse = taxiRecInAirport
      .filter(tu => tu(6).toInt == 0)
      .cache()
    println("机场范围内的空车记录数量:" + taxiNotUse.count())

    val file: File = new File("output.txt")

    val out: OutputStreamWriter = new OutputStreamWriter((new FileOutputStream(file)))
    out.write("有效记录个数：" + distinTaxiRecRDD.count() + '\n')
    out.write("机场范围内的载客记录数量:" + taxiInUse.count() + '\n')
    out.write("机场范围内的空车记录数量:" + taxiNotUse.count() + '\n')
    out.close()
  }
}