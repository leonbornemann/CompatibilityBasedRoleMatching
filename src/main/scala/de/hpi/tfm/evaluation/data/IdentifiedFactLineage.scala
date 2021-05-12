package de.hpi.tfm.evaluation.data

import de.hpi.tfm.compatibility.graph.fact.IDBasedTupleReference
import de.hpi.tfm.data.socrata.{JsonReadable, JsonWritable}
import de.hpi.tfm.data.tfmp_input.table.nonSketch.FactLineageWithHashMap

case class IdentifiedFactLineage(id:String, factLineage: FactLineageWithHashMap) extends JsonWritable[IdentifiedFactLineage] {

}

object IdentifiedFactLineage extends JsonReadable[IdentifiedFactLineage] {

  def getIDString(subdomain:String,id:IDBasedTupleReference) = {
    subdomain +"_"+ id.toString
  }

}
