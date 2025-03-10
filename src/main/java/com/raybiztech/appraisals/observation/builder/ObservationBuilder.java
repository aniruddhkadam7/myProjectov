package com.raybiztech.appraisals.observation.builder;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.exceptions.InvalidRatingsException;
import com.raybiztech.appraisals.observation.business.Observation;
import com.raybiztech.appraisals.observation.business.ObservationGraph;
import com.raybiztech.appraisals.observation.dto.ObservationDTO;
import com.raybiztech.appraisals.observation.dto.ObservationGraphsDTO;
import com.raybiztech.date.Date;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.recruitment.business.DocType;
import com.raybiztech.recruitment.business.Document;
import com.raybiztech.recruitment.dto.PersonDTO;

@Component
public class ObservationBuilder {
	@Autowired
	EmployeeBuilder builder;

	Logger logger = Logger.getLogger(ObservationBuilder.class);

	public ObservationDTO convertFormObservationToObservationDTO(
			Observation observation) {

		ObservationDTO observationDTO = null;
		if (observation != null) {
			observationDTO = new ObservationDTO();
			Employee employee = observation.getEmployee();
			PersonDTO personDTO = new PersonDTO();
			personDTO.setPersonId(employee.getEmployeeId());
			personDTO.setFirstName(employee.getFirstName());
			personDTO.setLastName(employee.getLastName());
			observationDTO.setEmployee(personDTO);

			Employee employee1 = observation.getAddedBy();
			PersonDTO adedby = new PersonDTO();
			adedby.setPersonId(employee1.getEmployeeId());
			adedby.setFirstName(employee1.getFirstName());
			adedby.setLastName(employee1.getLastName());
			observationDTO.setAddedBy(adedby);

			observationDTO.setComment(observation.getComments());
			observationDTO
					.setDate(observation.getDate().toString("dd/MM/yyyy"));
			observationDTO.setDescription(observation.getDescription());
			observationDTO.setId(observation.getId());
			observationDTO.setRating(observation.getRating());
			if (observation.getObsFilePath() != null) {
				String folderPath = observation.getObsFilePath();// here we r
																	// getting
																	// full path
																	// structure
																	// of
																	// uploaded
																	// file
				String downloadPath = folderPath.substring(folderPath
						.lastIndexOf("/") + 1);// here we are getting only file
												// name
				observationDTO.setObsFilePath(downloadPath);
			}
			observationDTO.setEmpName(observation.getEmployee().getFirstName()
					+ " " + observation.getEmployee().getLastName());
			observationDTO.setAddedByUser(observation.getAddedBy()
					.getFirstName()
					+ " "
					+ observation.getAddedBy().getLastName());
			observationDTO.setObservationMonth(observation
					.getObservationMonth() != null ? observation
					.getObservationMonth().toString("MMM-yyyy") : null);

		}

		return observationDTO;
	};

	public Observation convertFormObservationDTOtoObservation(
			ObservationDTO observationDTO) {

		Observation observation = null;
		if (observationDTO != null) {
			// if the rating is not in between 0 to 10 we are throwing error
			// message
			if (observationDTO.getRating() < 0
					|| observationDTO.getRating() > 10) {
				throw new InvalidRatingsException();
			}
			observation = new Observation();
			Employee employe = new Employee();
			employe.setEmployeeId(observationDTO.getEmployee().getPersonId());
			observation.setEmployee(employe);
			Employee addedby = new Employee();
			addedby.setEmployeeId(observationDTO.getAddedBy().getPersonId());
			observation.setAddedBy(addedby);

			// Here we are doing this because some times text editor is
			// appending "&#10;" to content
			observation.setComments(observationDTO.getComment().replace(
					"&#10;", ""));
			try {
				observation.setDate(Date.parse(observationDTO.getDate(),
						"dd/MM/yyyy"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			observation.setDescription(observationDTO.getDescription());
			observation.setId(observationDTO.getId());
			observation.setRating(observationDTO.getRating());

			try {
				observation.setObservationMonth(Date.parse(
						observationDTO.getObservationMonth(), "MM/yyyy"));
			} catch (ParseException e) {

				e.printStackTrace();
			}

			observation.setDate(new Date());
		}
		return observation;

	}

	public List<ObservationDTO> convertFormObservationToObservationDTOList(
			List<Observation> set) {

		List<ObservationDTO> observationDTOs = new ArrayList<ObservationDTO>();

		if (set != null) {

			for (Observation observation : set) {

				ObservationDTO observationDTO = new ObservationDTO();

				Employee employee = observation.getEmployee();
				PersonDTO personDTO = new PersonDTO();
				personDTO.setPersonId(employee.getEmployeeId());
				personDTO.setFirstName(employee.getFirstName());
				personDTO.setLastName(employee.getLastName());
				observationDTO.setEmployee(personDTO);
				observationDTO.setEmpName(observation.getEmployee()
						.getFirstName()
						+ " "
						+ observation.getEmployee().getLastName());
				observationDTO.setAddedByUser(observation.getAddedBy()
						.getFirstName()
						+ " "
						+ observation.getAddedBy().getLastName());

				Employee employee1 = observation.getAddedBy();
				PersonDTO adedby = new PersonDTO();
				adedby.setPersonId(employee1.getEmployeeId());
				adedby.setFirstName(employee1.getFirstName());
				adedby.setLastName(employee1.getLastName());
				observationDTO.setAddedBy(adedby);

				
				//here we are doing this because text editor adding some special characters 
				//We are also restricting this while adding observation 
				//For adding already added observation 
				
				observationDTO.setComment(observation.getComments().replace(
						"&#10;", ""));
				observationDTO.setDate(observation.getDate().toString(
						"dd MMM yyyy"));
				observationDTO.setId(observation.getId());
				observationDTO.setRating(observation.getRating());
				observationDTO.setDescription(observation.getDescription());

				observationDTO.setObservationMonth(observation
						.getObservationMonth() != null ? observation
						.getObservationMonth().toString("MMM-yyyy") : null);

				if (observation.getObsFilePath() != null) {
					String folderPath = observation.getObsFilePath();
					// here we are getting full path structure
					String downloadPath = folderPath.substring(folderPath
							.lastIndexOf("/") + 1);// here we are getting only
													// file name
					observationDTO.setObsFilePath(downloadPath);
				}
				observationDTOs.add(observationDTO);
			}
		}
		return observationDTOs;

	}

	// Observation graph
	/*
	 * public ObservationGraphsDTO getGraphs(List<Observation> set) {
	 * 
	 * ObservationGraphsDTO observationDTOs = null; if (set != null) {
	 * List<String> series = new ArrayList<String>();
	 * series.add("Observations"); observationDTOs = new ObservationGraphsDTO();
	 * observationDTOs.setSeries(series);
	 * 
	 * List<ObservationGraph> graphs = new ArrayList<ObservationGraph>();
	 * ObservationGraph graph = null; List<Integer> yaxis = null; for
	 * (Observation observation : set) { graph = new ObservationGraph(); yaxis =
	 * new ArrayList<Integer>();
	 * graph.setX(observation.getObservationMonth().toString("MMM-yy"));
	 * yaxis.add(observation.getRating()); graph.setY(yaxis); graphs.add(graph);
	 * }
	 * 
	 * observationDTOs.setSeries(series); observationDTOs.setData(graphs);
	 * 
	 * } return observationDTOs; }
	 */
	public List<ObservationDTO> ToDTOList(List<Observation> observations) {
		List<ObservationDTO> ObservationDTOList = null;
		if (observations != null) {
			ObservationDTOList = new ArrayList<ObservationDTO>();
			for (Observation obser : observations) {
				ObservationDTOList
						.add(convertFormObservationToObservationDTO(obser));
			}
		}
		return ObservationDTOList;
	}

	public List<ObservationDTO> convertEmployeeToObservationDTO(
			List<Employee> employees, String selectedMonthAndYEar) {
		ObservationDTO observationDTO = null;

		List<ObservationDTO> observationDTOs = new ArrayList<ObservationDTO>();
		if (employees != null) {

			for (Employee employee : employees) {

				observationDTO = new ObservationDTO();
				PersonDTO personDTO = new PersonDTO();
				personDTO.setPersonId(employee.getEmployeeId());
				personDTO.setFirstName(employee.getFirstName());
				personDTO.setLastName(employee.getLastName());
				observationDTO.setEmployee(personDTO);
				observationDTO.setEmpName(employee.getFirstName() + " "
						+ employee.getLastName());
				// if(observationDTO.getObservationMonth() != null){
				try {
					observationDTO.setObservationMonth(DateParser.toDate(
							selectedMonthAndYEar).toString("MMM-yyyy"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// }
				// To get the Reporting manager name
				observationDTO.setAddedByUser(employee.getManager()
						.getFullName());

				observationDTOs.add(observationDTO);

			}
		}

		return observationDTOs;
	}

}
