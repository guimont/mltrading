package com.mltrading.models.parser.impl;


/**
 * Created by gmo on 29/12/2015.
 */

@Deprecated
public class HistoryExtendDataInsee {

    //Statistiques des transports - Immatriculations de voitures particulières neuves - Données brutes
    private static String immat = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=001641573";

    //Indice des prix à la consommation (mensuel, ensemble des ménages, métropole + DOM, base 1998) - Ensemble
    private static String indiceConsoM = "http://www.bdm.insee.fr/bdm2/affichageSeries?idbank=000639196&codeGroupe=142";

    //Demandeurs d'emploi inscrits en fin de mois à Pôle emploi - Catégories A, B et C - France métropolitaine - Série CVS-CJO
    private static String unemployment = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=001572360";

    //Indice brut de la production industrielle (base 100 en 2010) - Industrie manufacturière, industries extractives et autres (NAF rév. 2, niveau A10, poste BE)
    private static String industryProd = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=001654235";

    //Indice de prix de production de l'industrie française pour le marché français - Prix de base - A10 BE - Ensemble de l'industrie - Base 2010 - (FB0ABE0000)
    private static String industryProdFr = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=001652631";

    //Indice de prix d'importation de produits industriels - Toutes zones - A10 BE - Ensemble de l'industrie sauf gestion eau, dépollution - Base 2010 - (A0TABE0000)
    private static String industryProdImportation = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=001653081";

    //Indice des prix de production de la construction neuve à usage d'habitation - Base 2010
    private static String newConstruction_t = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=001667524";

    //Cours du Dollar US par rapport à l'Euro (1 Dollar US) - Moyenne mensuelle
    private static String euroDollar = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=000642334";

    //Cours de la Livre Sterling par rapport à l'Euro (1 Livre Sterling) - Moyenne mensuelle
    private static String euroLivre = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=000642327";

    //Exportations FAB de la France y c. DOM - Destination : Le monde entier - Ensemble CAF/FAB hors matériel militaire - Données de collecte - NAF rév. 2
    private static String exportBalance = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=001568783";

    //Importations CAF de la France y c. DOM - Provenance : Le monde entier - Ensemble CAF/FAB hors matériel militaire - Données de collecte - NAF rév. 2
    private static String importBalance = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=001569268";

    //Indices des prix internationaux des matières premières importées - Ensemble - En euros - Base 100 en 2000
    private static String importRawMatIndice = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=000810636";

    //Indices des prix internationaux des matières premières importées - Produits industriels - En euros - Base 100 en 2000
    private static String importRawMatIndusIndice = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=000810648";

    //Indices Moody's des prix internationaux des matières premières importées - Ensemble - En devises - Base 100 le 31/12/1931
    private static String moodyRawMat = "http://www.insee.fr/fr/bases-de-donnees/bsweb/serie.asp?idbank=000495554";


    static String refCode = "tbody";

    public void loader(String code, String name ,String url) {


    }

}
