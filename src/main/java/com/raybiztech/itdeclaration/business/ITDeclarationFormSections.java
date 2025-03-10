package com.raybiztech.itdeclaration.business;

import java.io.Serializable;
import java.util.Set;

public class ITDeclarationFormSections implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long itSectionsId;
	private Boolean isOld;
	private Section section;
	private Set<ITDeclarationFormInvestments> itInvests;
	public Boolean getIsOld() {
		return isOld;
	}

	public void setIsOld(Boolean isOld) {
		this.isOld = isOld;
	}

	public Long getItSectionsId() {
		return itSectionsId;
	}

	public void setItSectionsId(Long itSectionsId) {
		this.itSectionsId = itSectionsId;
	}

	public Set<ITDeclarationFormInvestments> getItInvests() {
		return itInvests;
	}

	public void setItInvests(Set<ITDeclarationFormInvestments> itInvests) {
		this.itInvests = itInvests;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isOld == null) ? 0 : isOld.hashCode());
		result = prime * result
				+ ((itInvests == null) ? 0 : itInvests.hashCode());
		result = prime * result
				+ ((itSectionsId == null) ? 0 : itSectionsId.hashCode());
		result = prime * result + ((section == null) ? 0 : section.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ITDeclarationFormSections other = (ITDeclarationFormSections) obj;
		if (isOld == null) {
			if (other.isOld != null)
				return false;
		} else if (!isOld.equals(other.isOld))
			return false;
		if (itInvests == null) {
			if (other.itInvests != null)
				return false;
		} else if (!itInvests.equals(other.itInvests))
			return false;
		if (itSectionsId == null) {
			if (other.itSectionsId != null)
				return false;
		} else if (!itSectionsId.equals(other.itSectionsId))
			return false;
		if (section == null) {
			if (other.section != null)
				return false;
		} else if (!section.equals(other.section))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ITDeclarationFormSections [itSectionsId=" + itSectionsId
				+ ", isOld=" + isOld + ", section=" + section + ", itInvests="
				+ itInvests + "]";
	}

	
}
