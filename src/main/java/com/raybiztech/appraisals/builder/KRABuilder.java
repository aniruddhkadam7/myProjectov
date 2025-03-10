package com.raybiztech.appraisals.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.KRA;
import com.raybiztech.appraisals.business.KraWithWeightage;
import com.raybiztech.appraisals.dto.KRADTO;
import com.raybiztech.appraisals.dto.KraWithWeightageDTO;

@Component("kraBuilder")
public class KRABuilder {

	@Autowired
	KPIBuilder kpiBuilder;

	public KPIBuilder getKpiBuilder() {
		return kpiBuilder;
	}

	public void setKpiBuilder(KPIBuilder kpiBuilder) {
		this.kpiBuilder = kpiBuilder;
	}

	Logger log = Logger.getLogger(KRABuilder.class);

	public KRADTO createKRADTO(KRA kra) {

		KRADTO kradto = new KRADTO();
		if (kra != null) {
			kradto.setId(kra.getKraId());
			kradto.setKraName(kra.getKraName());
			kradto.setDescription(kra.getDescription());
			kradto.setKpis(kpiBuilder.getKPIDtoList(kra.getKpis()));
		}
		return kradto;
	}

	public KRA createKRAEntity(KRADTO kradto) {
		KRA kra = new KRA();
		if (kradto != null) {
			kra.setKraId(kradto.getId());
			kra.setKraName(kradto.getKraName());
			kra.setDescription(kradto.getDescription());
			kra.setKpis(kpiBuilder.getKPIList(kradto.getKpis()));
		}
		return kra;
	}

	public List<KRADTO> getKRADtoList(List<KRA> kras) {

		List<KRADTO> kradtoList = new ArrayList<KRADTO>();
		for (KRA kra : kras) {
			KRADTO kradto = new KRADTO();
			kradto.setId(kra.getKraId());
			kradto.setKraName(kra.getKraName());
			kradto.setDescription(kra.getDescription());
			kradto.setKpis(kpiBuilder.getKPIDtoList(kra.getKpis()));
			kradtoList.add(kradto);
		}
		log.info("no. of kras in system: " + kradtoList.size());
		return kradtoList;
	}

	public List<KRA> getKRAList(List<KRADTO> kradtos) {

		List<KRA> kraList = new ArrayList<KRA>();
		for (KRADTO kradto : kradtos) {
			KRA kra = new KRA();
			if (kradto != null) {
				kra.setKraId(kradto.getId());
				kra.setKraName(kradto.getKraName());
				kra.setDescription(kradto.getDescription());
				kra.setKpis(kpiBuilder.getKPIList(kradto.getKpis()));
			}
			kraList.add(kra);
		}
		return kraList;
	}

	public List<KRADTO> getKRADTOListForDesignationMapping(List<KRA> kras) {
		List<KRADTO> kradtos = new ArrayList<KRADTO>();
		for (KRA kra : kras) {
			KRADTO kradto = new KRADTO();
			kradto.setId(kra.getKraId());
			;
			kradto.setKraName(kra.getKraName());
			kradto.setDescription(kra.getDescription());
			KraWithWeightage kw = kra.getKraWithWeightage();
			KraWithWeightageDTO kwdto = new KraWithWeightageDTO();
			if (kw != null) {
				kwdto.setWeightage(kw.getWeightage());
				kwdto.setId(kw.getKraWithWeightageId());
			}
			kradto.setKraWithWeightageDTO(kwdto);
			kradtos.add(kradto);
		}
		return kradtos;
	}
}
