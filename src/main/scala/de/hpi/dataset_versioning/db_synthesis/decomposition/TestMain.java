package de.hpi.dataset_versioning.db_synthesis.decomposition;

import de.hpi.dataset_versioning.data.DatasetInstance;
import de.hpi.dataset_versioning.data.simplified.RelationalDataset;
import de.hpi.dataset_versioning.io.IOService;

import java.io.File;
import java.time.LocalDate;

public class TestMain {

    public static void main(String[] args) {
        System.out.println("Hi Hazar, this works");
        System.out.println(IOService.socrataDir());
        IOService.socrataDir_$eq("new SOcrata dir");
        System.out.println(IOService.socrataDir());
        File f = IOService.getJoinCandidateFile();
        System.out.println(f.getAbsolutePath());
        //you will need:
        //RelationalDataset ds = IOService.loadSimplifiedRelationalDataset(new DatasetInstance("your dataset id", LocalDate.parse("the latest version of this dataset")));
    }
}
