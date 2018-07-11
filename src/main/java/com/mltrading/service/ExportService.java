package com.mltrading.service;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mltrading.models.util.ExportTools;
import com.mltrading.models.util.ServiceExport;
import org.springframework.stereotype.Service;
@Service
public class ExportService {

    Injector injector = Guice.createInjector(new ServiceExport());
    ExportTools export =  injector.getInstance( ExportTools.class );

    public void exportRaw() {
        export.exportRaw();
    }

    public void exportStock() {
        export.exportStock();
    }

    public void exportSector() {
        export.exportSector();
    }

    public void exportIndice() {
        export.exportIndice();
    }

    public void exportVcac() {
        export.exportVcac();
    }

    public void exportAT() {
        export.exportAT();
    }


    public void importRaw() {
        export.importRaw();
    }

    public void importStock() {
        export.importStock();
    }

    public void importSector() {
        export.importSector();
    }

    public void importIndice() {
        export.importIndice();
    }

    public void importVcac() {
        export.importVcac();
    }


    public void importAT() {
        export.importAT();
    }
}
