package com.mltrading.models.util;

import com.google.inject.AbstractModule;
import com.mltrading.models.util.impl.ExportToolsImpl;



public class ServiceExport extends AbstractModule {


    @Override
    protected void configure() {
        bind(ExportTools.class).to(ExportToolsImpl.class);
    }

}
