package com.sspl.demo.service.mapper;

import com.sspl.demo.domain.Dashboard;
import com.sspl.demo.service.dto.DashboardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dashboard} and its DTO {@link DashboardDTO}.
 */
@Mapper(componentModel = "spring")
public interface DashboardMapper extends EntityMapper<DashboardDTO, Dashboard> {}
