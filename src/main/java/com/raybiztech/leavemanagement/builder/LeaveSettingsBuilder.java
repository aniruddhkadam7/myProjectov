package com.raybiztech.leavemanagement.builder;

import org.springframework.stereotype.Component;

import com.raybiztech.leavemanagement.business.LeaveCycleMonth;
import com.raybiztech.leavemanagement.business.LeaveSettingsLookup;
import com.raybiztech.leavemanagement.dto.LeaveSettingsDTO;

@Component("leaveSettingsBuilder")
public class LeaveSettingsBuilder {

	public LeaveSettingsLookup createLeaveSettingsEntity(LeaveSettingsDTO settingDTO) {
		// TODO Auto-generated method stub

		LeaveSettingsLookup leaveSettings = new LeaveSettingsLookup();
		if (settingDTO != null) {
			leaveSettings.setId(settingDTO.getId());
			leaveSettings.setMaxLeavesEarned(settingDTO.getMaxLeavesEarned());
			leaveSettings.setProbationPeriod(settingDTO.getProbationPeriod());
			leaveSettings.setPayrollCutoffDate(settingDTO
					.getPayrollCutoffDate());
			leaveSettings.setLeaveCycleMonth(LeaveCycleMonth.valueOf(settingDTO
					.getLeaveCycleMonth()));
			leaveSettings.setLeavesPerYear(settingDTO.getLeavesPerYear());
			leaveSettings.setMaxAccrualPerYear(settingDTO.getMaxAccrualPerYear());

		}

		return leaveSettings;
	}

	public LeaveSettingsDTO createLeaveSettingsDTO(LeaveSettingsLookup leaveSettings) {
		// TODO Auto-generated method stub

		LeaveSettingsDTO leaveSettingsDTO = new LeaveSettingsDTO();
		if (leaveSettings != null) {
			leaveSettingsDTO.setId(leaveSettings.getId());
			leaveSettingsDTO.setMaxLeavesEarned(leaveSettings
					.getMaxLeavesEarned());

			leaveSettingsDTO.setLeaveCycleMonth(leaveSettings
					.getLeaveCycleMonth().toString());
			leaveSettingsDTO.setProbationPeriod(leaveSettings
					.getProbationPeriod());
			leaveSettingsDTO.setPayrollCutoffDate(leaveSettings
					.getPayrollCutoffDate());
			leaveSettingsDTO.setLeavesPerYear(leaveSettings.getLeavesPerYear());
			leaveSettingsDTO.setMaxAccrualPerYear(leaveSettings.getMaxAccrualPerYear());

		}

		return leaveSettingsDTO;
	}
}
