package com.mltrading.models.stock;

import com.mltrading.domain.AbstractAuditingEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gmo on 06/12/2015.
 */

@Document(collection = "JHI_STOCK")
public class Stock  extends AbstractAuditingEntity implements Serializable {

    @Id
    private Company company;
    private Bilan bilan;
    private List<String> indice;
    private String sector;
    private List<String> rawMat;



    public Bilan getBilan() {
        return bilan;
    }

    public void setBilan(Bilan bilan) {
        this.bilan = bilan;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<String> getIndice() {
        return indice;
    }

    public void setIndice(List<String> indice) {
        this.indice = indice;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public List<String> getRawMat() {
        return rawMat;
    }

    public void setRawMat(List<String> rawMat) {
        this.rawMat = rawMat;
    }
}
