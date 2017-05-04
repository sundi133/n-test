import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}
import scala.io.Source
import scala.util.{Failure, Success, Try}

object N_2_UrlCounter {

  val sdf = new SimpleDateFormat("MM/dd/yy");
  sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

  def main(args: Array[String]) {

    Try {
      Source.fromFile(args(0).toString).getLines().toList
        .map(w => {
          val s = w.split("\\|")
          (s(0).toLong * 1000, s(1))
        })
        .map(w => (sdf.format(new Date(w._1)), w._2))
        .groupBy(w => w._1 + "|" + w._2)
        .map(w => {
          val d = w._1.split("\\|")
          (d(0), d(1), w._2.size)
        })
        .toList.sortWith( _._1 > _._1 )
        .groupBy(w => w._1)
        .map(w => {
          println(w._1 +" GMT")
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
