package com.sspl.demo.service.impl;

import com.sspl.demo.domain.Dashboard;
import com.sspl.demo.repository.DashboardRepository;
import com.sspl.demo.service.DashboardService;
import com.sspl.demo.service.dto.DashboardDTO;
import com.sspl.demo.service.mapper.DashboardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.sspl.demo.domain.Dashboard}.
 */
@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final DashboardRepository dashboardRepository;

    private final DashboardMapper dashboardMapper;

    public DashboardServiceImpl(DashboardRepository dashboardRepository, DashboardMapper dashboardMapper) {
        this.dashboardRepository = dashboardRepository;
        this.dashboardMapper = dashboardMapper;
    }

    @Override
    public DashboardDTO save(DashboardDTO dashboardDTO) {
        log.debug("Request to save Dashboard : {}", dashboardDTO);
        Dashboard dashboard = dashboardMapper.toEntity(dashboardDTO);
        dashboard = dashboardRepository.save(dashboard);
        return dashboardMapper.toDto(dashboard);
    }

    @Override
    public DashboardDTO update(DashboardDTO dashboardDTO) {
        return null;
    }

    @Override
    public Optional<DashboardDTO> partialUpdate(DashboardDTO dashboardDTO) {
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DashboardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dashboards");
        return dashboardRepository.findAll(pageable).map(dashboardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DashboardDTO> findOne(Long id) {
        log.debug("Request to get Dashboard : {}", id);
        return dashboardRepository.findById(id).map(dashboardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dashboard : {}", id);
        dashboardRepository.deleteById(id);
    }
}
