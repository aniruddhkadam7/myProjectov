package com.raybiztech.appraisals.builder;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.KPI;
import com.raybiztech.appraisals.business.KRA;
import com.raybiztech.appraisals.dto.KPIDTO;
import com.raybiztech.appraisals.dto.KRADTO;

@Component("kpiBuilder")
public class KPIBuilder {

    Logger log = Logger.getLogger(KPIBuilder.class);

    @Autowired
    KRABuilder kraBuilder;
    @Autowired
    KPIRatingBuilder kpiRatingBuilder;

    public KRABuilder getKraBuilder() {
        return kraBuilder;
    }

    public void setKraBuilder(KRABuilder kraBuilder) {
        this.kraBuilder = kraBuilder;
    }

    public KPIRatingBuilder getKpiRatingBuilder() {
        return kpiRatingBuilder;
    }

    public void setKpiRatingBuilder(KPIRatingBuilder kpiRatingBuilder) {
        this.kpiRatingBuilder = kpiRatingBuilder;
    }

    public KPIDTO createKPIDTO(KPI kpi) {

        KPIDTO kpidto = new KPIDTO();

        if (kpi != null) {
            kpidto.setId(kpi.getKpiId());
            kpidto.setKpiName(kpi.getKpiName());
            KRA kra = kpi.getKra();
            if (kra != null) {
                KRADTO kradto = kraBuilder.createKRADTO(kra);
                kpidto.setKradto(kradto);
            }
            kpidto.setDescription(kpi.getDescription());
        }

        return kpidto;
    }

    public KPI createKPIEntity(KPIDTO kpidto) {

        KPI kpi = new KPI();
        if (kpidto != null) {
            kpi.setKpiId(kpidto.getId());
            kpi.setKpiName(kpidto.getKpiName());
            kpi.setDescription(kpidto.getDescription());
            kpi.setKra(kraBuilder.createKRAEntity(kpidto.getKradto()));
        }
        return kpi;
    }

    public Set<KPIDTO> getKPIDtoList(Set<KPI> kpis) {

        Set<KPIDTO> kpiDtoSet = new HashSet<KPIDTO>();
        if (kpis != null) {
            for (KPI kpi : kpis) {

                KPIDTO kdto = new KPIDTO();
                kdto.setId(kpi.getKpiId());
                kdto.setKpiName(kpi.getKpiName());
                kdto.setDescription(kpi.getDescription());
                kdto.setKpiRatingDTOs(kpiRatingBuilder.getKPIRatingDtos(kpi
                        .getKpiRatings()));
                kpiDtoSet.add(kdto);
            }
        }
        return kpiDtoSet;
    }

    public Set<KPI> getKPIList(Set<KPIDTO> kpidtos) {

        Set<KPI> kpiSet = new HashSet<KPI>();

        if (kpidtos != null) {
            for (KPIDTO kpidto : kpidtos) {
                KPI kpi = new KPI();

                kpi.setKpiId(kpidto.getId());
                kpi.setKpiName(kpidto.getKpiName());
                kpi.setDescription(kpidto.getDescription());
                kpi.setKpiRatings(kpiRatingBuilder.getKPIRatings(kpidto
                        .getKpiRatingDTOs()));
                KRA kra = new KRA();
                if (kpidto.getKradto() != null) {
                    kra.setKraId(kpidto.getKradto().getId());
                }
                kpi.setKra(kra);
                kpiSet.add(kpi);
            }
        }
        return kpiSet;
    }
}
