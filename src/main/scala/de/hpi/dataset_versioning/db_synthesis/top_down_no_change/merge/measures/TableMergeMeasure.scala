package de.hpi.dataset_versioning.db_synthesis.top_down_no_change.merge.measures

import de.hpi.dataset_versioning.data.simplified.Attribute
import de.hpi.dataset_versioning.db_synthesis.database.SynthesizedDatabaseTable
import de.hpi.dataset_versioning.db_synthesis.top_down_no_change.decomposition.normalization.DecomposedTable

trait TableMergeMeasure  {

  def calculate(t1:DecomposedTable, t2:DecomposedTable, mapping:Map[Attribute,Attribute]):Int //TODO: later we might want to make this a double?

  def calculate(synthTable:SynthesizedDatabaseTable,t:DecomposedTable, mapping:Map[Attribute,Attribute]):Int //TODO: later we might want to make this a double?

}
