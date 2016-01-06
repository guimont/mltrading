package com.mltrading.models.stock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mltrading.domain.AbstractAuditingEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gmo on 06/12/2015.
 */

@Document(collection = "JHI_STOCK")
public class Stock  implements Serializable {


    @JsonIgnore
    private Company company;

    private String marche;
    private String indice;
    private String codeif;
    @Id
    private String code;

    private String bloomberg;
    private String reuters;
    private String eligib;
    private String titleNb;
    private String capitalization;
    private String ownFounds;
    private String debt;
    private String netDebt;

    @JsonIgnore
    private Bilan bilan;

    //private List<String> indice;
    private String sector;

    @JsonIgnore
    private List<String> rawMat;


    public Stock(String marche, String indice, String codeif, String code, String bloomberg, String reuters, String eligib, String titleNb, String capitalization, String ownFounds, String debt, String netDebt, String sector) {
        this.marche = marche;
        this.indice = indice;
        this.codeif = codeif;
        this.code = code;
        this.bloomberg = bloomberg;
        this.reuters = reuters;
        this.eligib = eligib;
        this.titleNb = titleNb;
        this.capitalization = capitalization;
        this.ownFounds = ownFounds;
        this.debt = debt;
        this.netDebt = netDebt;
        this.sector = sector;
    }

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

    /*public List<String> getIndice() {
        return indice;
    }

    public void setIndice(List<String> indice) {
        this.indice = indice;
    }*/

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

    public String getMarche() {
        return marche;
    }

    public void setMarche(String marche) {
        this.marche = marche;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getCodeif() {
        return codeif;
    }

    public void setCodeif(String codeif) {
        this.codeif = codeif;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBloomberg() {
        return bloomberg;
    }

    public void setBloomberg(String bloomberg) {
        this.bloomberg = bloomberg;
    }

    public String getReuters() {
        return reuters;
    }

    public void setReuters(String reuters) {
        this.reuters = reuters;
    }

    public String getEligib() {
        return eligib;
    }

    public void setEligib(String eligib) {
        this.eligib = eligib;
    }

    public String getTitleNb() {
        return titleNb;
    }

    public void setTitleNb(String titleNb) {
        this.titleNb = titleNb;
    }

    public String getCapitalization() {
        return capitalization;
    }

    public void setCapitalization(String capitalization) {
        this.capitalization = capitalization;
    }

    public String getOwnFounds() {
        return ownFounds;
    }

    public void setOwnFounds(String ownFounds) {
        this.ownFounds = ownFounds;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getNetDebt() {
        return netDebt;
    }

    public void setNetDebt(String netDebt) {
        this.netDebt = netDebt;
    }
}
