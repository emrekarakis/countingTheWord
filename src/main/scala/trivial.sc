

val a = "asdasdasdadasdasdc"
a.groupBy(_.toChar).mapValues(_.size)

val b = Map("a" -> 2)
b.updated("a",b("a")+4)

val map1 = collection.immutable.HashMap(1 -> 11 , 2 -> 12)
val map2 = collection.immutable.HashMap(1 -> 11 , 2 -> 12)
map1.merged(map2)({ case ((k,v1),(_,v2)) => (k,v1+v2) })



  def countWords(text: String) = {
    var counts: Map[String, Int] = Map.empty[String, Int]
    for (rawWord: String <- text.split("[ ,!.]+-)(/\|][{§½$#£}&%^':;*?_=<>\"...")) {
      val word: String = rawWord.toLowerCase
      val oldCount: Int =
        if (counts.contains(word)) counts(word)
        else 0
      counts += (word -> (oldCount + 1))
    }
    counts
  }




  // took 101 secs (10M lines)
  // work on one character at a time
  def countLines1(source: Source): Long = {
    var newlineCount = 0L
    for {
      c <- source
      if c=="\n"
    } newlineCount += 1
    newlineCount
  }
 // took 22 secs (10M lines)
 // use getLines to count the number of lines
 def countLines2(source: Source): Long = {
   var newlineCount = 0L
   for (line <- source.getLines) {
     newlineCount += 1
   }
   newlineCount
 }

  def countLines3(source: io.BufferedSource): Long = {
    var newlineCount = 0L
    for (line <- source.getLines) {
      for {
        c <- line
        if c== "\n"
      } newlineCount += 1
    }
    newlineCount
  }



