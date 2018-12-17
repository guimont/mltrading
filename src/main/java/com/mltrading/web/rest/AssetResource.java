package com.mltrading.web.rest;


import com.mltrading.assetmanagement.AssetManagement;
import com.mltrading.assetmanagement.CacheAssetMemory;
import com.mltrading.security.AuthoritiesConstants;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;


@RestController
@RequestMapping("/api")
public class AssetResource {

    @RequestMapping(value = "/asset/all",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.USER)
    public List<AssetManagement> getAllAsset() {

        return  CacheAssetMemory.getInstance().getAssetManagementList();
    }



}
