package com.nastech.upmureport.domain.dto;

import java.time.LocalDateTime;

import com.nastech.upmureport.domain.entity.Project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProjectDto {
	private Integer projId;
	private String projName;
	private String projCaleGubun;
	private String projSubject;
	private String projDesc;
	private String startYear, startMonth, startDay;
	private String endYear, endMonth, endDay;
	private Integer projProgress;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	
	public ProjectDto() {
		this.projId = -1;
	}
	
	private LocalDateTime getStartDate() {
		if (startDate != null) return startDate;
		LocalDateTime startLdt = 
				LocalDateTime.of(
						Integer.valueOf(startYear.trim()), 
						Integer.valueOf(startMonth.trim()), 
						Integer.valueOf(startDay.trim()), 9, 0);
		
		return startLdt;
	}
	
	private LocalDateTime getEndDate() {
		if (endDate != null) return endDate;
		LocalDateTime endLdt = 
				LocalDateTime.of(
						Integer.valueOf(endYear.trim()), 
						Integer.valueOf(endMonth.trim()), 
						Integer.valueOf(endDay.trim()), 18, 0);
		return endLdt;
	}
	
	public Project toEntity() {
		Project project = Project.builder()
				.projName(projName)
				.projCaleGubun(projCaleGubun)
				.projSubject(projSubject)
				.projDesc(projDesc)
				.projStartDate(getStartDate())
				.projEndDate(getEndDate())
				.build();
		
		if (projId != null && projId != 0) project.setProjId(projId);
		
		return project;
	}
}