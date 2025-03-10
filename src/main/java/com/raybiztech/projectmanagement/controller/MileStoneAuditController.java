package com.raybiztech.projectmanagement.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.projectmanagement.dto.MileStoneAuditDTO;
import com.raybiztech.projectmanagement.service.ProjectService;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/milestoneaudit")
public class MileStoneAuditController {

	//private final ProjectService projectService;

	@Autowired
	ProjectService projectService;
	/*public MileStoneAuditController(ProjectService projectService) {

		this.projectService = projectService;
	}*/

	@RequestMapping(value = "/",params={"id"}, method = RequestMethod.GET)
	public @ResponseBody List<MileStoneAuditDTO> getAllMileStoneAuldit(
			@RequestParam Long id,HttpServletResponse httpServletResponse) {

		return this.projectService.getAllMileStoneHistory(id);
	}
}
