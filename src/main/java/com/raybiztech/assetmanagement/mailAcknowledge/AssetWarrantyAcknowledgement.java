package com.raybiztech.assetmanagement.mailAcknowledge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.assetmanagement.business.Asset;
import com.raybiztech.assetmanagement.dao.AssetManagementDAO;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.business.MailTemplate;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.mailtemplates.util.MailContentParser;

@Component("WarrantyAssetMailAcknowledgment")
public class AssetWarrantyAcknowledgement {

	@Autowired
	MailTemplatesDao mailTemplatesDao;

	@Autowired
	MailContentParser mailContentParser;

	@Autowired
	PropBean propBean;

	@Autowired
	AssetManagementDAO assetManagementDAOImpl;

	Logger logger = Logger.getLogger(AssetWarrantyAcknowledgement.class);

	public void sendWarrantyAssetEndDateNotification(MailTemplate mailTemplate,
			List<Asset> assets) {

		if (!assets.isEmpty() && mailTemplate.getTemplate() != null
				&& mailTemplate.getEmail() != null) {

			String tableheader = "<table width='600' border='1' bordercolor='black' cellspacing='0' cellpadding='5'>"
					+ "<thead>"
					+ "<tr>"
					+ "<th><center>Asset No.</center></th>"
					+ "<th><center>Asset Name</center></th>"
					+ "<th><center>Warranty End Date</center></th>"
					+ "</tr>"
					+ "</thead>" + "<tbody>" + "<tr>";

			String tableEnd = "</tr></tbody></table> <br><br> Thanks & Regards <br>Ray Business Technologies Pvt Ltd.";

			String tableData = "";

			for (Asset asset : assets) {
				tableData = tableData
						+ "<tr><td><center>"
						+ asset.getAssetNumber()
						+ "</center></td>"
						+ "<td><center>"
						+ asset.getProductSpecifications().getProduct()
								.getProductName() + "</center></td>"
						+ "<td><center>" + asset.getWarrantyEndDate()
						+ "</center></td></tr>";
			}

			String table = tableheader + tableData + tableEnd;

			Map<String, String> details = new HashMap<String, String>();
			details.put("regarding",
					"<![CDATA[ Asset Warranty End Date Alert ]]>");
			details.put("to", mailTemplate.getEmail());
			details.put("cc", "");
			details.put("bcc", "");

			String content = mailTemplate.getTemplate();

			content = content + "<br><br><br>" + table;

			if (content != null) {
				mailContentParser.parseAndSendMail(details, content);
			} else {
				throw new MailCantSendException(
						"Mail Content is not available for 'Asset Warranty End Date Alert' template Type");
			}
		} else {
			throw new MailCantSendException(
					"Mail Cant be sent for Asset Type "
							+ mailTemplate.getAssetType().getAssetType()
							+ "Beacuse Asset List or Mail template Or Email Id's are not Available");
		}

	}
}
