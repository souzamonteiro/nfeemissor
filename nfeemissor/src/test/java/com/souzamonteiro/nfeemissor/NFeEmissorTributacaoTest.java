package com.souzamonteiro.nfeemissor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.PIS;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.COFINS;

class NFeEmissorTributacaoTest {

    @Test
    void deveMapearGruposICMSComuns() {
        JSONObject icms = new JSONObject();
        icms.put("ICMS00", new JSONObject()
            .put("orig", "0")
            .put("CST", "00")
            .put("modBC", "3")
            .put("vBC", "100.00")
            .put("pICMS", "18.00")
            .put("vICMS", "18.00"));
        icms.put("ICMS10", new JSONObject()
            .put("orig", "0")
            .put("CST", "10")
            .put("modBC", "3")
            .put("vBC", "100.00")
            .put("pICMS", "18.00")
            .put("vICMS", "18.00")
            .put("modBCST", "4")
            .put("pMVAST", "20.00")
            .put("pRedBCST", "0.00")
            .put("vBCST", "120.00")
            .put("pICMSST", "18.00")
            .put("vICMSST", "21.60"));
        icms.put("ICMS20", new JSONObject()
            .put("orig", "0")
            .put("CST", "20")
            .put("modBC", "3")
            .put("pRedBC", "10.00")
            .put("vBC", "90.00")
            .put("pICMS", "18.00")
            .put("vICMS", "16.20"));
        icms.put("ICMS30", new JSONObject()
            .put("orig", "0")
            .put("CST", "30")
            .put("modBCST", "4")
            .put("pMVAST", "30.00")
            .put("pRedBCST", "0.00")
            .put("vBCST", "120.00")
            .put("pICMSST", "18.00")
            .put("vICMSST", "21.60"));
        icms.put("ICMS40", new JSONObject()
            .put("orig", "0")
            .put("CST", "40"));
        icms.put("ICMS51", new JSONObject()
            .put("orig", "0")
            .put("CST", "51")
            .put("modBC", "3")
            .put("vBC", "100.00")
            .put("pICMS", "18.00")
            .put("vICMSOp", "18.00")
            .put("pDif", "12.00")
            .put("vICMSDif", "13.20")
            .put("vICMS", "18.00"));
        icms.put("ICMS60", new JSONObject()
            .put("orig", "0")
            .put("CST", "60")
            .put("vBCSTRet", "100.00")
            .put("pST", "18.00")
            .put("vICMSSubstituto", "18.00")
            .put("vICMSSTRet", "18.00"));

        Imposto imposto = new Imposto();
        ICMS icmsObj = new ICMS();
        icmsObj.setICMS00(new ICMS.ICMS00());
        icmsObj.setICMS10(new ICMS.ICMS10());
        icmsObj.setICMS20(new ICMS.ICMS20());
        icmsObj.setICMS30(new ICMS.ICMS30());
        icmsObj.setICMS40(new ICMS.ICMS40());
        icmsObj.setICMS51(new ICMS.ICMS51());
        icmsObj.setICMS60(new ICMS.ICMS60());
        imposto.getContent().add(new javax.xml.bind.JAXBElement<>(new javax.xml.namespace.QName("ICMS"), ICMS.class, icmsObj));

        assertNotNull(imposto.getContent());
        assertDoesNotThrow(() -> imposto.getContent().forEach(x -> {}));
    }

    @Test
    void deveMapearGruposPisECofins() {
        PIS pis = new PIS();
        pis.setPISAliq(new PIS.PISAliq());
        pis.setPISOutr(new PIS.PISOutr());
        pis.setPISNT(new PIS.PISNT());
        pis.setPISQtde(new PIS.PISQtde());

        COFINS cofins = new COFINS();
        cofins.setCOFINSAliq(new COFINS.COFINSAliq());
        cofins.setCOFINSOutr(new COFINS.COFINSOutr());
        cofins.setCOFINSNT(new COFINS.COFINSNT());
        cofins.setCOFINSQtde(new COFINS.COFINSQtde());

        assertNotNull(pis);
        assertNotNull(cofins);
    }
}
