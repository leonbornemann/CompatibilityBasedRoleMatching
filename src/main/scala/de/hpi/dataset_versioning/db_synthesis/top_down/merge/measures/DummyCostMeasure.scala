package de.hpi.dataset_versioning.db_synthesis.top_down.merge.measures

import de.hpi.dataset_versioning.data.simplified.Attribute
import de.hpi.dataset_versioning.db_synthesis.top_down.decomposition.normalization.DecomposedTable

class DummyCostMeasure() extends TableMergeMeasure {

  override def calculate(t1: DecomposedTable, t2: DecomposedTable, mapping: Map[Attribute, Attribute]): Int = 0
}
