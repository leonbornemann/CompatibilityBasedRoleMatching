package de.hpi.dataset_versioning.db_synthesis.top_down.merge

import de.hpi.dataset_versioning.data.simplified.Attribute

case class TableMergeResult(columnMapping:Map[Attribute,Attribute], benefit:Int, cost:Int) {

}
