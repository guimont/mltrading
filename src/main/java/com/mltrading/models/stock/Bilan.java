package com.mltrading.models.stock;

/**
 * Created by gmo on 16/10/2015.
 */

@Deprecated
public class Bilan {

    // compte de resultat
    private int chiffre_affaire;
    private int produit_activite_ordinaire;
    private int resultat_operationnel;
    private int cout_endetement;
    private int quote_part_res;
    private int rn_result_abandonne;
    private int rn;
    private int rn_group;

    //bilan

    private int Ecart_acquisition;
    private int Immobilisations_incorporelles;
    private int Immobilisations_corporelles;
    private int Actifs_financiers_non_courants;
    private int Stocks_travaux_en_cours;
    private int Créances_clients;
    private int Autres_actifs;
    private int Trésorerie;
    private int Total_actif;
    private int Capitaux_propres;
    private int Provisions_pour_charges;
    private int Dettes_financières_non_courantes;
    private int Dettes_financières_courantes;
    private int Fournisseurs_comptes_rattaches;
    private int Autres_passifs;
    private int Total_passif;


    //ratio
    private int Résultat_net_par_action;
    private int Résultat_net_dilué_par_action;
    private int Marge_opérationnelle;
    private int Rentabilité_Financière;
    private int Ratio_endettement;
    private int Effectif_fin_année;
    private int Effectif_moyen ;

    //chiffre affaire
    private int CA_1trimestre;
    private int CA_2trimestre;
    private int CA_3trimestre;
    private int CA_4trimestre;
    private int CA_semestriel;
    private int CA_annuel;


    public int getRn_group() {
        return rn_group;
    }

    public void setRn_group(int rn_group) {
        this.rn_group = rn_group;
    }

    public int getChiffre_affaire() {
        return chiffre_affaire;
    }

    public void setChiffre_affaire(int chiffre_affaire) {
        this.chiffre_affaire = chiffre_affaire;
    }

    public int getProduit_activite_ordinaire() {
        return produit_activite_ordinaire;
    }

    public void setProduit_activite_ordinaire(int produit_activite_ordinaire) {
        this.produit_activite_ordinaire = produit_activite_ordinaire;
    }

    public int getResultat_operationnel() {
        return resultat_operationnel;
    }

    public void setResultat_operationnel(int resultat_operationnel) {
        this.resultat_operationnel = resultat_operationnel;
    }

    public int getCout_endetement() {
        return cout_endetement;
    }

    public void setCout_endetement(int cout_endetement) {
        this.cout_endetement = cout_endetement;
    }

    public int getQuote_part_res() {
        return quote_part_res;
    }

    public void setQuote_part_res(int quote_part_res) {
        this.quote_part_res = quote_part_res;
    }

    public int getRn_result_abandonne() {
        return rn_result_abandonne;
    }

    public void setRn_result_abandonne(int rn_result_abandonne) {
        this.rn_result_abandonne = rn_result_abandonne;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }

    public int getEcart_acquisition() {
        return Ecart_acquisition;
    }

    public void setEcart_acquisition(int ecart_acquisition) {
        Ecart_acquisition = ecart_acquisition;
    }

    public int getImmobilisations_incorporelles() {
        return Immobilisations_incorporelles;
    }

    public void setImmobilisations_incorporelles(int immobilisations_incorporelles) {
        Immobilisations_incorporelles = immobilisations_incorporelles;
    }

    public int getImmobilisations_corporelles() {
        return Immobilisations_corporelles;
    }

    public void setImmobilisations_corporelles(int immobilisations_corporelles) {
        Immobilisations_corporelles = immobilisations_corporelles;
    }

    public int getActifs_financiers_non_courants() {
        return Actifs_financiers_non_courants;
    }

    public void setActifs_financiers_non_courants(int actifs_financiers_non_courants) {
        Actifs_financiers_non_courants = actifs_financiers_non_courants;
    }

    public int getStocks_travaux_en_cours() {
        return Stocks_travaux_en_cours;
    }

    public void setStocks_travaux_en_cours(int stocks_travaux_en_cours) {
        Stocks_travaux_en_cours = stocks_travaux_en_cours;
    }

    public int getCréances_clients() {
        return Créances_clients;
    }

    public void setCréances_clients(int créances_clients) {
        Créances_clients = créances_clients;
    }

    public int getAutres_actifs() {
        return Autres_actifs;
    }

    public void setAutres_actifs(int autres_actifs) {
        Autres_actifs = autres_actifs;
    }

    public int getTrésorerie() {
        return Trésorerie;
    }

    public void setTrésorerie(int trésorerie) {
        Trésorerie = trésorerie;
    }

    public int getTotal_actif() {
        return Total_actif;
    }

    public void setTotal_actif(int total_actif) {
        Total_actif = total_actif;
    }

    public int getCapitaux_propres() {
        return Capitaux_propres;
    }

    public void setCapitaux_propres(int capitaux_propres) {
        Capitaux_propres = capitaux_propres;
    }

    public int getProvisions_pour_charges() {
        return Provisions_pour_charges;
    }

    public void setProvisions_pour_charges(int provisions_pour_charges) {
        Provisions_pour_charges = provisions_pour_charges;
    }

    public int getDettes_financières_non_courantes() {
        return Dettes_financières_non_courantes;
    }

    public void setDettes_financières_non_courantes(int dettes_financières_non_courantes) {
        Dettes_financières_non_courantes = dettes_financières_non_courantes;
    }

    public int getDettes_financières_courantes() {
        return Dettes_financières_courantes;
    }

    public void setDettes_financières_courantes(int dettes_financières_courantes) {
        Dettes_financières_courantes = dettes_financières_courantes;
    }

    public int getFournisseurs_comptes_rattaches() {
        return Fournisseurs_comptes_rattaches;
    }

    public void setFournisseurs_comptes_rattaches(int fournisseurs_comptes_rattaches) {
        Fournisseurs_comptes_rattaches = fournisseurs_comptes_rattaches;
    }

    public int getAutres_passifs() {
        return Autres_passifs;
    }

    public void setAutres_passifs(int autres_passifs) {
        Autres_passifs = autres_passifs;
    }

    public int getTotal_passif() {
        return Total_passif;
    }

    public void setTotal_passif(int total_passif) {
        Total_passif = total_passif;
    }

    public int getRésultat_net_par_action() {
        return Résultat_net_par_action;
    }

    public void setRésultat_net_par_action(int résultat_net_par_action) {
        Résultat_net_par_action = résultat_net_par_action;
    }

    public int getRésultat_net_dilué_par_action() {
        return Résultat_net_dilué_par_action;
    }

    public void setRésultat_net_dilué_par_action(int résultat_net_dilué_par_action) {
        Résultat_net_dilué_par_action = résultat_net_dilué_par_action;
    }

    public int getMarge_opérationnelle() {
        return Marge_opérationnelle;
    }

    public void setMarge_opérationnelle(int marge_opérationnelle) {
        Marge_opérationnelle = marge_opérationnelle;
    }

    public int getRentabilité_Financière() {
        return Rentabilité_Financière;
    }

    public void setRentabilité_Financière(int rentabilité_Financière) {
        Rentabilité_Financière = rentabilité_Financière;
    }

    public int getRatio_endettement() {
        return Ratio_endettement;
    }

    public void setRatio_endettement(int ratio_endettement) {
        Ratio_endettement = ratio_endettement;
    }

    public int getEffectif_fin_année() {
        return Effectif_fin_année;
    }

    public void setEffectif_fin_année(int effectif_fin_année) {
        Effectif_fin_année = effectif_fin_année;
    }

    public int getEffectif_moyen() {
        return Effectif_moyen;
    }

    public void setEffectif_moyen(int effectif_moyen) {
        Effectif_moyen = effectif_moyen;
    }

    public int getCA_1trimestre() {
        return CA_1trimestre;
    }

    public void setCA_1trimestre(int CA_1trimestre) {
        this.CA_1trimestre = CA_1trimestre;
    }

    public int getCA_2trimestre() {
        return CA_2trimestre;
    }

    public void setCA_2trimestre(int CA_2trimestre) {
        this.CA_2trimestre = CA_2trimestre;
    }

    public int getCA_3trimestre() {
        return CA_3trimestre;
    }

    public void setCA_3trimestre(int CA_3trimestre) {
        this.CA_3trimestre = CA_3trimestre;
    }

    public int getCA_4trimestre() {
        return CA_4trimestre;
    }

    public void setCA_4trimestre(int CA_4trimestre) {
        this.CA_4trimestre = CA_4trimestre;
    }

    public int getCA_semestriel() {
        return CA_semestriel;
    }

    public void setCA_semestriel(int CA_semestriel) {
        this.CA_semestriel = CA_semestriel;
    }

    public int getCA_annuel() {
        return CA_annuel;
    }

    public void setCA_annuel(int CA_annuel) {
        this.CA_annuel = CA_annuel;
    }
}
