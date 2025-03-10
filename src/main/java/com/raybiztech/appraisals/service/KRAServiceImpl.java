/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.service;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.builder.KPIBuilder;
import com.raybiztech.appraisals.builder.KRABuilder;
import com.raybiztech.appraisals.business.DesignationKras;
import com.raybiztech.appraisals.business.KPI;
import com.raybiztech.appraisals.business.KPIRating;
import com.raybiztech.appraisals.business.KRA;
import com.raybiztech.appraisals.business.KraWithWeightage;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.kra.KRADao;
import com.raybiztech.appraisals.dto.KPIDTO;
import com.raybiztech.appraisals.dto.KRADTO;

@Service("/kraService")
@Transactional
public class KRAServiceImpl implements KRAService {

    @Autowired
    DAO dao;
    @Autowired
    KRADao kraDao;
    @Autowired
    KRABuilder kraBuilder;
    @Autowired
    KPIBuilder kpiBuilder;

    Logger logger = Logger.getLogger(KRAServiceImpl.class);

    @Override
    public void createKRA(KRADTO kraDto) throws Exception {
        KRA kra = kraBuilder.createKRAEntity(kraDto);
        List<KraWithWeightage> kraWithWeightages = kraDao.removeWithWeightage();
        for (KraWithWeightage kraWithWeightage : kraWithWeightages) {
            dao.delete(kraWithWeightage);
        }
        List<DesignationKras> designations = dao.get(DesignationKras.class);
        for(DesignationKras designation : designations){
            KraWithWeightage kraWithWeightage = new KraWithWeightage();
            kraWithWeightage.setKra(kra);
            kraWithWeightage.setWeightage(0.0);
            kraWithWeightage.setDesignation(designation);
            dao.save(kraWithWeightage);
        }
        dao.save(kra);
    }

    @Override
    public void removeKRA(KRADTO kradto) throws Exception {
        logger.info("KRA id in removeKRA :" + kradto.getId());
        KRA kra = kraDao.findBy(KRA.class, kradto.getId());
        List<KraWithWeightage> kraWithWeightages = kraDao
                .getKRAWithWeightages(kradto.getId());
        for (KraWithWeightage kraWithWeightage : kraWithWeightages) {
            kraWithWeightage.setKra(null);
            kraWithWeightage.setDesignation(null);
            dao.saveOrUpdate(kraWithWeightage);
        }

        dao.delete(kra);

    }

    @Override
    public void addKPI(KPIDTO kpidto, Long kraId) throws Exception {
        KRA kra = dao.findBy(KRA.class, kraId);
        KPI kpi = kpiBuilder.createKPIEntity(kpidto);
        kpi.setKra(kra);
        dao.saveOrUpdate(kpi);
    }

    @Override
    public void removeKPI(Long kpiId) {
        KPI kpi = dao.findBy(KPI.class, kpiId);
        Set<KPIRating> kpiRatings = kpi.getKpiRatings();
        kpi.getKpiRatings().removeAll(kpiRatings);
        KRA kra = kpi.getKra();
        kra.getKpis().remove(kpi);
        dao.saveOrUpdate(kra);
    }

    @Override
    public List<KRADTO> getAllKras() {
        List<KRA> kras = dao.get(KRA.class);
        return kraBuilder.getKRADtoList(kras);
    }

    @Override
    public boolean isExistingKRA(String kraName) throws Exception {
        KRA kra = dao.findByKRAName(KRA.class, kraName);
        return kra != null;
    }

    @Override
    public Set<KPIDTO> getAllKpis(String kraName) throws Exception {
        KRA kra = dao.findByKRAName(KRA.class, kraName);
        KRADTO kradto = kraBuilder.createKRADTO(kra);
        return kradto.getKpis();
    }

    @Override
    public boolean isExistingKPI(String kraName, String kpiName)
            throws Exception {
        KRA kra = dao.findByKRAName(KRA.class, kraName);
        KPI kpi = dao.findByKPIName(KPI.class, kpiName);
        Set<KPI> kpis = kra.getKpis();
        return kpis.contains(kpi);
    }
}
