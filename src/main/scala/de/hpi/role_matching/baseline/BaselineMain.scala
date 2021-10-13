package de.hpi.role_matching.baseline

import com.typesafe.scalalogging.StrictLogging
import de.hpi.role_matching.GLOBAL_CONFIG
import de.hpi.role_matching.clique_partitioning.RoleMerge
import de.hpi.role_matching.clique_partitioning.SparseGraphCliquePartitioningMain.{args, maxRecallSetting}
import de.hpi.role_matching.compatibility.graph.representation.slim.VertexLookupMap
import de.hpi.role_matching.compatibility.graph.representation.vertex.IdentifiedFactLineage
import de.hpi.role_matching.evaluation.clique.CliqueAnalyser
import de.hpi.role_matching.evaluation.clique.CliqueBasedEvaluationMain.resultDir

import java.io.PrintWriter
import java.time.LocalDate
import scala.io.Source

object BaselineMain extends App with StrictLogging{
  logger.debug(s"Called with ${args.toIndexedSeq}")
  val vertexLookupMap = VertexLookupMap.fromJsonFile(args(0))
  val dataSource = args(1)
  GLOBAL_CONFIG.setDatesForDataSource(dataSource)
  val trainTimeEnd = LocalDate.parse(args(2))
  val resultDir = args(3)
  val methodIsValueSet = args(4) == "valueSet"
  if(!methodIsValueSet)
    assert(args(4) == "valueSequence")
  val maxRecallEdgeSetFile = args(5)
  val prCliques = new PrintWriter(resultDir + "/cliques.csv")
  val prCliquesTruePositivesToReview = new PrintWriter(resultDir + "/cliques_To_Review_True_positives.csv")
  val prCliquesRestToReview = new PrintWriter(resultDir + "/cliques_To_Review_Rest.csv")
  val prEdges = new PrintWriter(resultDir + "/edges.csv")
  val grouped = vertexLookupMap.posToLineage.groupMap(ifl => {
    if(methodIsValueSet)
      ifl._2.factLineage.toFactLineage.nonWildcardValueSetBefore(trainTimeEnd)
    else
      ifl._2.factLineage.toFactLineage.nonWildcardValueSequenceBefore(trainTimeEnd)
  })(_._1)
  val edgesInMaxRecall = Source.fromFile(maxRecallEdgeSetFile)
    .getLines()
    .toIndexedSeq
    .tail
    .map(s => s.split(",")(1))
    .toSet
  val analyzer = new CliqueAnalyser(prCliques,prCliquesTruePositivesToReview,prCliquesRestToReview,prEdges,vertexLookupMap,trainTimeEnd,None,None,Some(edgesInMaxRecall))
  analyzer.serializeSchema()
  var groupsDone = 0
  val method = if(methodIsValueSet) "valueSetBaseline" else "valueSequenceBaseline"
  grouped.values
    .withFilter(_.size>1)
    .foreach(matched => {
    val rm = RoleMerge(matched.toSet,Double.MinValue)
    if(matched.size*matched.size>100000){
      logger.debug(s"Processing large group with ${matched.size} vertices")
    }
      analyzer.addResultTuple(rm,"NA",method)
    if(matched.size*matched.size>100000){
      logger.debug(s"Done with large group with ${matched.size} vertices")
    }
    groupsDone+=1
    if(groupsDone%1000 == 0){
      logger.debug(s"Finished $groupsDone (${100*groupsDone / grouped.size.toDouble}%)")
    }
  })
  analyzer.printResults()
  prEdges.close()
  prCliques.close()

}
