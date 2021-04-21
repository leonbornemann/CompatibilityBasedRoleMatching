package de.hpi.tfm.data.wikipedia.infobox.transformed

import com.typesafe.scalalogging.StrictLogging
import de.hpi.tfm.data.wikipedia.infobox.original.{InfoboxRevision, InfoboxRevisionHistory}
import de.hpi.tfm.data.wikipedia.infobox.statistics.WikipediaInfoboxStatistiicsGatherer

import java.io.{File, PrintWriter}

object WikipediaInfoboxValueHistoryCreationMain extends App with StrictLogging {
  //https://owncloud.hpi.de/s/H2juuaquPE7BUAV/download?path=%2F&files=enwiki-20190901-pages-meta-history27.xml-p57135490p57467999.output.json.7z
  //  val file1 = Source.fromFile("/home/leon/data/dataset_versioning/WIkipedia/infoboxes/owncloud files")
  //    .getLines()
  //    .toIndexedSeq
  //    .filter(_.contains("enwiki-20190901-pages-meta"))
  //    .map(l => "https://owncloud.hpi.de/s/H2juuaquPE7BUAV/download?path=%2F&files=" + l.split("Aktionen")(0))
  //    .foreach(println)
  //  assert(false)
  val file = args(0)
  val resultDir = new File(args(1))
  val statGatherer = if(args.size==3) Some( new WikipediaInfoboxStatistiicsGatherer(new File(args(2)))) else None
  val objects = InfoboxRevision.fromJsonObjectPerLineFile(file)
  objects.foreach(_.checkIntegrity())
  val revisionHistories = InfoboxRevisionHistory.getFromRevisionCollection(objects)
  revisionHistories.foreach(rh => rh.integrityCheck())
  logger.debug(s"Found ${revisionHistories.size} infobox histories to process")
  var finished = 0
  val resultFile = resultDir.getAbsolutePath + "/" + WikipediaInfoboxValueHistory.getFilenameForBucket(new File(file).getName)
  val pr = new PrintWriter(resultFile)
  revisionHistories
    .foreach(r => {
      val res = r.toWikipediaInfoboxValueHistories
      res.foreach(_.appendToWriter(pr,false,true))
      if(statGatherer.isDefined) statGatherer.get.addToFile(res)
      finished += 1
      if (finished % 100 == 0) {
        logger.debug(s"Finished $finished infobox histories")
      }
    })
  if(statGatherer.isDefined) statGatherer.get.closeFile()
  pr.close()
}
