package de.hpi.dataset_versioning.db_synthesis.baseline.decomposition

import de.hpi.dataset_versioning.data.{JsonReadable, JsonWritable}
import de.hpi.dataset_versioning.io.DBSynthesis_IOService

@SerialVersionUID(3L)
case class DecomposedTemporalTableIdentifier(subdomain:String,viewID:String,bcnfID:Int,associationID:Option[Int]) extends Serializable with JsonWritable[DecomposedTemporalTableIdentifier]{

  override def toString: String = viewID + "." + bcnfID + (if(associationID.isDefined) "_" + associationID.get.toString else "")

  def compositeID: String = subdomain + "." + viewID + "." + bcnfID + (if(associationID.isDefined) "_" + associationID.get.toString else "")

}

object DecomposedTemporalTableIdentifier extends JsonReadable[DecomposedTemporalTableIdentifier]{
  def loadAllAssociationsWithChanges() = {
    val file = DBSynthesis_IOService.getAssociationsWithChangesFile()
    fromJsonObjectPerLineFile(file)
  }

  def fromFilename(fileName: String) = {
    val tokens1 = fileName.split("\\.")
    val viewIDs = tokens1.zipWithIndex.filter(t => t._1.size == 9 && t._1.charAt(4) == '-')
    val viewIDIndex = viewIDs.head._2
    val subdomain = tokens1.slice(0,viewIDIndex).reduce(_ + "." + _)//tokens1(0) //can contain dots
    val viewID = tokens1(viewIDIndex)
    if(tokens1(viewIDIndex+1).contains("_")){
      val tokens2 = tokens1(viewIDIndex+1).split("_")
      val bcnfID = tokens2(0).toInt
      val associationID = Some(tokens2(1).toInt)
      DecomposedTemporalTableIdentifier(subdomain,viewID,bcnfID,associationID)
    } else{
      val bcnfID = tokens1(viewIDIndex+1).toInt
      DecomposedTemporalTableIdentifier(subdomain,viewID,bcnfID,None)
    }
  }
}
