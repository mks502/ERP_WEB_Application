package com.nastech.upmureport.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nastech.upmureport.domain.dto.PdirDto;
import com.nastech.upmureport.domain.dto.ProjectDto;
import com.nastech.upmureport.domain.entity.Member;
import com.nastech.upmureport.domain.entity.MemberProject;
import com.nastech.upmureport.domain.entity.Pdir;
import com.nastech.upmureport.domain.entity.Project;
import com.nastech.upmureport.domain.entity.support.Prole;
import com.nastech.upmureport.domain.entity.support.Pstat;
import com.nastech.upmureport.domain.repository.MemberProjectRepository;
import com.nastech.upmureport.domain.repository.MemberRepository;
import com.nastech.upmureport.domain.repository.PdirRepository;
import com.nastech.upmureport.support.Utils;

@Service
public class ProjectService {
	@Autowired
	private MemberRepository mr;
	@Autowired
	private MemberProjectRepository mpr;
	@Autowired
	private PdirRepository dr;
	@Autowired
	private PdirService ds;
	
	/**
	 * 넘겨받은 프로젝트 정보에 의거해 프로젝트를 등록합니다. 이 때 요청한 유저는 프로젝트에 책임자로 소속합니다.
	 * @param pDto 프로젝트 등록할 정보
	 * @return 등록자와 프로젝트 연결 객체, 해당 유저가 존재하지 않을 경우 익셉션
	 */
	@Transactional
	public ProjectDto register(ProjectDto pDto) throws NoSuchElementException {
		Member member = null;

		Project project = Project.builder()
				.pname(pDto.getPname())
				.description(pDto.getDescription())
				.stDate(pDto.getStDate())
				.edDate(pDto.getEdDate())
				.build();
		
		BigInteger BigIntegerPid = null;
		String pid = pDto.getPid();
		if (pid != null && pid.equals("")) {
			BigIntegerPid = Utils.StrToBigInt(pid);
			project.setPid(BigIntegerPid);
		}
		
		Long mid = Long.valueOf(pDto.getMid());
		try {
			member = mr.findById(mid).get();
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException();
		}
		
		Pstat ps = null;
		try {
			ps = Pstat.valueOf(pDto.getPstat());
		} catch (IllegalArgumentException iae) {
			ps = Pstat.대기;
		}
		
		MemberProject mp = MemberProject.builder()
				.member(member)
				.project(project)
				.pstat(ps)
				.prole(Prole.책임자)
				.progress(pDto.getProgress())
				.build();
		
		Pdir rootDir = Pdir.builder()
				.dname("/")
				.parentDir(null)
				.member(member)
				.project(project)
				.build();
		
		dr.save(rootDir);
		return new ProjectDto(mpr.save(mp));
	}
	

	/**
	 * 프로젝트 DTO로부터 MemberProject, Project를 수정합니다.
	 * @param pDto
	 * @return 변경한 MemberProject 오브젝트
	 */
	@Transactional
	public ProjectDto correct(ProjectDto pDto) {
		Project p = Project.builder()
				.pid(Utils.StrToBigInt(pDto.getPid()))
				.build();
		
		Member m = Member.builder()
				.mid(Long.valueOf(pDto.getMid()))
				.build();
		
		//원본 유저, 프로젝트
		MemberProject mp = mpr.findOneByMemberAndProject(m, p);
		
		Pstat ps;
		try {
			ps = Pstat.valueOf(pDto.getPstat());
		} catch (IllegalArgumentException iae) {
			ps = mp.getPstat();
		}
		mp.setPstat(ps);
		
		Prole pr;
		try {
			pr = Prole.valueOf(pDto.getProle());
		} catch (IllegalArgumentException iae) {
			pr = mp.getProle();
		}
		mp.setProle(pr);
		Utils.overrideEntity(mp.getProject(), pDto);
		return new ProjectDto(mpr.save(mp));
	}
	
	/**
	 * 멤버 아이디에 따라 ProjectDto 리스트를 반환합니다.
	 * @param mid
	 * @return pDTOs
	 */
	public List<ProjectDto> listByMid(String mid) {
		Member m = Member.builder()
				.mid(Long.valueOf(mid))
				.build();
		
		List<MemberProject> mpList = mpr.findAllByMemberAndDflagFalse(m);
		
		List<ProjectDto> pDTOs = new ArrayList<ProjectDto>();
		for (MemberProject mp : mpList) {
			ProjectDto pDTO = new ProjectDto(mp);
			List<PdirDto> dirs = ds.listByPid(pDTO.getPid());
			pDTO.setDirs(dirs);
			pDTOs.add(pDTO);
		}
		
		return pDTOs;
	}
	
	@Transactional
	public ProjectDto disable(ProjectDto pDto) throws NoSuchElementException {
		Member m = Member.builder()
				.mid(Long.valueOf(pDto.getMid()))
				.build();
		
		Project p = Project.builder()
				.pid(Utils.StrToBigInt(pDto.getPid()))
				.build();
		
		MemberProject mp = mpr.findOneByMemberAndProject(m, p);
		mp.setDflag(true);
		
		return new ProjectDto(mpr.save(mp));
	}
}