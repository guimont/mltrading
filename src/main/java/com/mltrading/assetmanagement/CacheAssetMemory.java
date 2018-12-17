package com.mltrading.assetmanagement;


import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class CacheAssetMemory {
    List<AssetManagement> assetManagementList = new ArrayList<>();
    private static CacheAssetMemory cacheAssetMemoryInstance;

    //private constructor.
    private CacheAssetMemory(){

        //Prevent form the reflection api.
        if (cacheAssetMemoryInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public synchronized static CacheAssetMemory getInstance(){
        if (cacheAssetMemoryInstance == null){ //if there is no instance available... create new one
            cacheAssetMemoryInstance = new CacheAssetMemory();
        }

        return cacheAssetMemoryInstance;
    }



    public List<AssetManagement> getAssetManagementList() {
        return assetManagementList;
    }

    public void setAssetManagementList(List<AssetManagement> assetManagementList) {
        this.assetManagementList = assetManagementList;
    }
}
