/**
 * Created by emrekarakis on 22/12/15.
 */

import java.util.concurrent.TimeoutException
import scala.concurrent.ExecutionContext.Implicits.global
import java.io.FileNotFoundException
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source


object  CountingTheWordsInParallel {

  def main(args: Array[String]) {

    val filename = "src/sample"
  try{
    val lines: List[Future[Map[String, Int]]] =Source.fromFile(filename).getLines().map { line: String =>

      val futureValue: Future[Map[String, Int]] = Future {

        val firstMap: Map[String, Int] = countWords(line)

        firstMap
      }
      futureValue
    }.toList

    val futureOfListOfMap: Future[List[Map[String, Int]]] = Future.sequence(lines)

    val futureResult: Future[Map[String, Int]] = futureOfListOfMap.map { each=>

     // println(s"each: $each")

      val value: Map[String, Int] = each.foldLeft(Map.empty[String, Int]) { (k, v) =>

        combineMap(k,v)

      }.toMap
      value
    }
    try{val result: Map[String, Int] = Await.result(futureResult, Duration(5000, "seconds"))
    println(s"result: $result")

    result
    }catch {
      case e: TimeoutException=> e.printStackTrace()
    }

  }catch {
    case e: FileNotFoundException => println("File Does not found")
  }
   Thread.sleep(5000)

  }

    def combineMap(map1:Map[String,Int],map2:Map[String,Int]):Map[String,Int] = {
      val resVal: Map[String, Int] =(map1.keySet++map2.keySet).map{ e=>

        (e,map1.getOrElse(e,0)+map2.getOrElse(e,0))

      }.toMap

    resVal

    }
  def countWords(text: String): Map[String, Int] = {
    var counts: Map[String, Int] = Map.empty[String, Int]
    for (rawWord: String <- text.split("[ ,!.]+?")) {
      val word: String = rawWord.toLowerCase
      val oldCount: Int =
        if (counts.contains(word)) counts(word)
        else 0
      counts += (word -> (oldCount + 1))
    }
    counts
  }



}
