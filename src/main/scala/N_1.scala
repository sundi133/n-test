import org.apache.spark.{SparkConf, SparkContext}

object N_1 {

  def main(args: Array[String]) {

    val (input , output , cluster) = (
              Option(args(0)).getOrElse("/Users/jsundi/Downloads/device.txt"),
              Option(args(1)).getOrElse("/Users/jsundi/Downloads/devicedataf"),
              Option(args(2)).getOrElse("local")
      )

    val sc = new SparkContext(new SparkConf().setMaster(cluster).setAppName("Device Stats"))

    val data  = sc.textFile(input)
      .map(w => {
        val d1 = w.split("=")(1).trim
        val d2 = d1.substring(1, d1.size-1).split(",")
        (d2(0),d2(1),List(d2(2).trim.toInt))
      }
      )
    //try to use cache here or persist to make sure data is not twice in the stages as RDDs immutable

    //device counts to calculate poor score
    val deviceCounts = data
       .map(w => {
         (w._1, w._3)
       }
       )
      .reduceByKey((a,b) => a:::b)
        .map(w => {
          (w._1, (w._2.foldLeft(0)(_+_) / w._2.size))
        })


    //because of dynamic ips , its quite possible that an ip is allocated to multiple devices across time windows so cannot do map join
    //can optimize here with dataframes, DF are more optimied for joins and underlying optimizations done by tungstan and spark compiler
   val topDevicePoorRatio = data
       .map ( w => {
          (w._1+"|"+w._2,1)
       })
      .reduceByKey(_+_)
        .map(w => {
          val d = w._1.split("\\|")
          (d(0),d(1))
        })
      .keyBy(w => w._1)
       .join(deviceCounts)
        .map(w => (w._2._1._2, List(w._2._2.toInt)))
        .reduceByKey((a, b) => a:::b)
        .map(w => (w._1 ,  (w._2.toList.filter(_ <= 50).size.toDouble/w._2.size) )  )
        .sortBy(_._2, false)
        .top(1)

    sc.parallelize(topDevicePoorRatio.toSeq)
      .repartition(1)
      .saveAsTextFile(output)

    sc.stop();
  }

}