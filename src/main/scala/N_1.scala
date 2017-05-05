import org.apache.spark.{SparkConf, SparkContext}

object N_1 {

  def main(args: Array[String]) {

    val (input , output , cluster) = (
              Option(args(0)).getOrElse("resources/input"),
              Option(args(1)).getOrElse("resources/output"),
              Option(args(2)).getOrElse("local")
      )

    val sc = new SparkContext(new SparkConf().setMaster(cluster).setAppName("Device Stats"))

    val data  = sc.textFile(input)
      .map(w => {
        val d1 = w.split("=")(1).trim
        val d2 = d1.substring(1, d1.size-1).split(",")
        (d2(0),d2(1),(d2(2).trim.toInt,1))
      }
      )

    //top device types with poor score
    val topDevicePoorRatio = data
       .map(w => {
         (w._1+"|"+ w._2, w._3)
       }
       )
      .reduceByKey((a,b) => (a._1+b._1,a._2+b._2))
      .mapValues(w => w._1/w._2)
      .map(w => (w._1.split("\\|")(1), w._2 <=50 match {
        case true => (1,1)
        case _ => (0,1)
      } ))
      .reduceByKey((a,b) => {
        (b._1 <= 50) match {
          case true => (a._1+ b._1 ,a._2+b._2)
          case _  => (a._1,a._2+b._2)
        }
      })
      .mapValues(w => w._1.toDouble/w._2)
      .sortBy(_._2, false)
      .top(1)


    sc.parallelize(topDevicePoorRatio.toSeq)
      .repartition(1)
      .saveAsTextFile(output)

    sc.stop();
  }

}