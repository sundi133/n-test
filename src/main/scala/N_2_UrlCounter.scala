import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}

import scala.io.Source
import scala.util.{Failure, Success, Try}

/*
Complexity O( nlogn ) due to sorting mainly, can use Radix sort to bring complexity down in linear time.
O(n+k) k = 8 for date sorts and k=200 considering domain urls will never go beyond 200.
The list can be passed to radix sort implementation for wherever sorting is required with the keys on which sorting will be done.
 */

object N_2_UrlCounter {

  val sdf = new SimpleDateFormat("MM/dd/yyyy");
  sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

  def main(args: Array[String]) {

    Try {
      Source.fromFile(Option(args(0)).getOrElse("resources/data1")).getLines().toList
        .map(w => {
          val s = w.split("\\|")
          (sdf.format(new Date(s(0).toLong * 1000l)), s(1))
        }).groupBy(w => w._1 + "|" + w._2)
        .map(w => {
          val d = w._1.split("\\|")
          (d(0), d(1), w._2.size)
        })
        .groupBy(_._1)
        .toList.sortWith( _._1  < _._1 )
        .map(w => {
          println( w._1 +" GMT")
          w._2.toList
            .sortWith(_._3 > _._3)
            .foreach(w => {
              println(w._2 + " " + w._3)
            }
            )
        })
    } match {
      case Success(lines) =>
      case Failure(ex) => println(s"Problem File not found, please pass proper file path as argument to main : ${ex.getMessage}")
    }
  }
}
