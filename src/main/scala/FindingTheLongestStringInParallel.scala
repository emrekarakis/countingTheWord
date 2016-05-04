import java.io.FileNotFoundException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source

/**
 * Created by emrekarakis on 22/12/15.
 */
object FindingTheLongestStringInParallel {
  def main(args: Array[String]) {
    val filename = "src/sample"
    try{
      val longest: List[Future[Array[String]]] =Source.fromFile(filename).getLines().map { line =>

        val fEachLine:Future[Array[String]] =Future{

          val listOfWord: Array[String] = for(rawWord: String <- line.split("[ ,!.]+"))
            yield (rawWord.toLowerCase)

          listOfWord
        }
        fEachLine
      }.toList

      val fVal: Future[Map[String, Int]] =longestWord(longest)
      val finalRes: Map[String, Int] =Await.result(fVal,Duration(10,"seconds"))
      println(s"en uzun string$finalRes")


    }catch {
      case e: FileNotFoundException => println("File Does not found")
    }
  }

  def longestWord(arrays: List[Future[Array[String]]]): Future[Map[String, Int]]={

    val futureOfListOfArray: Future[List[Array[String]]] =Future.sequence(arrays)
    val futureOfMap: Future[Map[String, Int]] =futureOfListOfArray.map{ listOfArray=>
      val combination: List[String] =listOfArray.flatten
      val str:String=combination.maxBy(_.length)
      Map(str -> str.length)
    }
    futureOfMap


  }
}
