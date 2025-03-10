package com.raybiztech.appraisalmanagement.business;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AppraisalKRAData implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8161387853761761371L;
	private Long id;
    private String name;
    private String description;
    private Set<AppraisalKPIData> kpis;
    private AppraisalForm appraisalForm;
    private Double designationKraPercentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AppraisalKPIData> getKpis() {
        return kpis;
    }

    public void setKpis(Set<AppraisalKPIData> kpis) {
        this.kpis = kpis;
    }

    public AppraisalForm getAppraisalForm() {
        return appraisalForm;
    }

    public void setAppraisalForm(AppraisalForm appraisalForm) {
        this.appraisalForm = appraisalForm;
    }
    

public Double getDesignationKraPercentage() {
		return designationKraPercentage;
	}

	public void setDesignationKraPercentage(Double designationKraPercentage) {
		this.designationKraPercentage = designationKraPercentage;
	}

	//    public Double getEmployeesumOfKpiRatingMeanvalue() {
//        Double sum = 0.0;
//        Double meanvalue;
//        for (AppraisalKPIData akpid : kpis) {
//            sum = sum + akpid.getEmployeeRating();
//        }
//        meanvalue = sum / kpis.size();
//        return meanvalue;
//    }
//
//    public Double getManagersumOfKpiRatingMeanvalue() {
//        Double sum = 0.0;
//        Double meanvalue;
//        for (AppraisalKPIData akpid : kpis) {
//            sum = sum + akpid.getManagerRating();
//        }
//        meanvalue = sum / kpis.size();
//        return meanvalue;
//    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1989, 97).append(name).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AppraisalKRAData other = (AppraisalKRAData) obj;
        return new EqualsBuilder().append(name, other.getName()).isEquals();
    }

    public Map<String, Float> getKRAAverageRating() {
//        Float empRating = 0.0f;
//        Float managerRating = 0.0f;
//        int empcount = 0;
//        int mgrcount = 0;
//        Map<String, Float> mapRating = new HashMap<String, Float>();
//        for (AppraisalKPIData akpid : getKpis()) {
//          
//
//            if (akpid.getEmployeeRating()!=null && akpid.getEmployeeRating() == 0) {
//                ++empcount;
//            }
//            
//            if (akpid.getManagerRating() != null && akpid.getManagerRating() == 0) {
//                ++mgrcount;
//            }
//            if (akpid.getManagerRating() != null){
//            managerRating = managerRating + akpid.getManagerRating();
//            }
//            if( akpid.getEmployeeRating()!=null){
//            empRating = empRating + akpid.getEmployeeRating();
//            }
//
//        }
//        Integer noOfKpi = getKpis().size();
//        mapRating.put("emprating", empRating / ((noOfKpi - empcount == 0) ? 1 : (noOfKpi - empcount)));
//        mapRating.put("mngrrating", managerRating / ((noOfKpi - mgrcount == 0) ? 1 : (noOfKpi - mgrcount)));
//
//        return mapRating;
return null;
    }
}
