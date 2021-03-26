import de.hpi.tfm.data.socrata.change.ChangeExporter
import de.hpi.tfm.data.socrata.change.temporal_tables.TemporalTable
import de.hpi.tfm.data.socrata.history.VersionHistoryConstruction
import de.hpi.tfm.io.IOService

object AttributeLineageTest extends App {

  IOService.socrataDir = "/home/leon/data/dataset_versioning/socrata/testDir/"
  val id1 = "AttributeLineageTest"
  val versionHistoryConstruction = new VersionHistoryConstruction()
  versionHistoryConstruction.constructVersionHistoryForSimplifiedFiles()
  new ChangeExporter().exportAllChanges(id1)
  val temporalTable = TemporalTable.load(id1)
  assert(temporalTable.attributes.exists(al => al.lineage.size==3 && al.lineage.toIndexedSeq(1)._2.isNE))
}
