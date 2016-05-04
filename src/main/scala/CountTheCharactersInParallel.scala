/**
 * Created by emrekarakis on 17/12/15.
 */
import scala.concurrent.ExecutionContext.Implicits.global
import java.io.FileNotFoundException
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source


object CountTheCharactersInParallel {

  def main(args: Array[String]) {
    val filename = "src/sample"

    try {
      val lines: Stream[Future[Map[Char, Int]]] = Source.fromFile(filename).getLines().map { line: String =>
        val futureValue: Future[Map[Char, Int]] = Future {
          val firstMap: Map[Char, Int] = line.groupBy(_.toChar).mapValues(_.size)
          firstMap
        }.recover{
          case e: Exception => {
            Map.empty[Char, Int]
          }
        }
        futureValue
      }.toStream

      val futureOfListOfMap: Future[Stream[Map[Char, Int]]] = Future.sequence(lines)

      val futureResult: Future[ Map[Char,Int]] = futureOfListOfMap.map { each: Stream[Map[Char,Int]] =>
        val value: Map[Char, Int] = each.foldLeft(Map.empty[Char, Int]) { (k,v) =>
          combineMaps(k,v)
        }
        value
      }

      val result: Map[Char, Int] = Await.result(futureResult, Duration(10, "seconds"))
       println(s"result: $result")
      result

    }catch {
      case e: FileNotFoundException => {
        println("File Does not found", e)
      }
      case e: Exception => {
        println("Getting the other type of Exception", e)
      }
    }


  }


  def combineMaps( map1: Map[Char,Int], map2: Map[Char,Int]):  Map[Char,Int] = {
    val resVal: Map[Char, Int] = (map1.keySet ++ map2.keySet).map{ e: Char  =>
      e -> (map1.getOrElse(e,0) + map2.getOrElse(e,0))
    }.toMap
     // (map1.keySet ++ map2.keySet).map (i=> (i,map1.getOrElse(i,0) + map2.getOrElse(i,0))).toMap
     resVal
  }


}


