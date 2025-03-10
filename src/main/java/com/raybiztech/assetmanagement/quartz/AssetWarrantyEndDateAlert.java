package com.raybiztech.assetmanagement.quartz;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.assetmanagement.business.Asset;
import com.raybiztech.assetmanagement.business.AssetType;
import com.raybiztech.assetmanagement.dao.AssetManagementDAO;
import com.raybiztech.assetmanagement.mailAcknowledge.AssetWarrantyAcknowledgement;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.business.MailTemplate;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;

@Transactional
@Component("assetWarrantyEndDateAlert")
public class AssetWarrantyEndDateAlert {

	@Autowired
	AssetManagementDAO assetManagementDAOImpl;

	@Autowired
	AssetWarrantyAcknowledgement assetWarrantyAcknowledgement;

	@Autowired
	MailTemplatesDao templatesDao;

	Logger logger = Logger.getLogger(AssetWarrantyEndDateAlert.class);

	public void AssetWarrantyEndDateAlertForNextFifteenDays()
			throws ParseException {

		Map<AssetType, Object> map = assetManagementDAOImpl
				.getAssetWhoseWarrantyEndDateisInNextFifteennDays();

		for (AssetType assetType : map.keySet()) {
			MailTemplate mailTemplate = templatesDao
					.getmailTemplateOfAssetType(assetType);

			if (mailTemplate != null) {

				List<Asset> assets = (List<Asset>) map.get(assetType);

				assetWarrantyAcknowledgement
						.sendWarrantyAssetEndDateNotification(mailTemplate,
								assets);
			} else {
				throw new MailCantSendException(
						"Mail Template For Asset Type Id" + assetType
								+ "is Not Available");
			}
		}

	}

}
