package com.raybiztech.appraisals.service;

import java.util.List;
import java.util.Set;

import com.raybiztech.appraisals.dto.KPIDTO;
import com.raybiztech.appraisals.dto.KRADTO;

public interface KRAService {

    /**
     * creates a new KRA
     *
     * @param kraName
     * @throws java.lang.Exception
     */
    void createKRA(KRADTO kraDto) throws Exception;

    /**
     * creates a new KRA with KPI's
     *
     * @param kraName
     * @param kpis
     * @throws java.lang.Exception
     */
    //void createKRA(String kraName, HashSet<KPI> kpis) throws Exception;

    /**
     * creates a new KRA with KPI's and description
     *
     * @param kraName
     * @param kpis
     * @param description
     * @throws java.lang.Exception
     */
    //void createKRA(String kraName,  String description,Set<KPI> kpis) throws Exception;

    /**
     * remove the specific KRA returns true if it is successfully removed else
     * returns false
     *
     * @param kraName
     * @throws java.lang.Exception
     */
    void removeKRA(KRADTO kradto) throws Exception;

    /**
     * Add kpis to existing KRA
     * @param kraName
     * @param kpis
     * @throws java.lang.Exception
     */
    void addKPI(KPIDTO kpidto, Long kraId) throws Exception;

    /**
     * remove kpis to existing KRA
     * @param kraName
     * @param kpis
     * @throws java.lang.Exception
     */
    void removeKPI(Long kpiId);

    /**
     * get All existing KRA's
     *
     * @return 
     */
    List<KRADTO> getAllKras();

    /**
     * this method checks whether KRA is existing or not
     *
     * returns true if exists else false
     *
     * @param kraName
     * @return 
     * @throws java.lang.Exception
     */
    boolean isExistingKRA(String kraName) throws Exception;

    /**
     * get All existing KPI's for given KRA
     *
     * @param kraName
     * @return 
     * @throws java.lang.Exception
     */
    Set<KPIDTO> getAllKpis(String kraName) throws Exception;

    /**
     * this method checks whether KPI is existing or not for that given KRA
     *
     * returns true if exists else false
     *
     * @param kraName
     * @param kpiName
     * @return 
     * @throws java.lang.Exception
     */
    boolean isExistingKPI(String kraName, String kpiName) throws Exception;

}
