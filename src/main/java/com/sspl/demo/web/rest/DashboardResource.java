package com.sspl.demo.web.rest;

import com.sspl.demo.repository.DashboardRepository;
import com.sspl.demo.service.DashboardService;
import com.sspl.demo.service.dto.DashboardDTO;
import com.sspl.demo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sspl.demo.domain.Dashboard}.
 */
@RestController
@RequestMapping("/api/dashboards")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    private static final String ENTITY_NAME = "dashboard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DashboardService dashboardService;

    private final DashboardRepository dashboardRepository;

    public DashboardResource(DashboardService dashboardService, DashboardRepository dashboardRepository) {
        this.dashboardService = dashboardService;
        this.dashboardRepository = dashboardRepository;
    }

    /**
     * {@code POST  /dashboards} : Create a new dashboard.
     *
     * @param dashboardDTO the dashboardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dashboardDTO, or with status {@code 400 (Bad Request)} if the dashboard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DashboardDTO> createDashboard(@RequestBody DashboardDTO dashboardDTO) throws URISyntaxException {
        log.debug("REST request to save Dashboard : {}", dashboardDTO);
        if (dashboardDTO.getId() != null) {
            throw new BadRequestAlertException("A new dashboard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dashboardDTO = dashboardService.save(dashboardDTO);
        return ResponseEntity.created(new URI("/api/dashboards/" + dashboardDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, dashboardDTO.getId().toString()))
            .body(dashboardDTO);
    }

    /**
     * {@code GET  /dashboards} : get all the dashboards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dashboards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DashboardDTO>> getAllDashboards(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Dashboards");
        Page<DashboardDTO> page = dashboardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dashboards/:id} : get the "id" dashboard.
     *
     * @param id the id of the dashboardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dashboardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DashboardDTO> getDashboard(@PathVariable("id") Long id) {
        log.debug("REST request to get Dashboard : {}", id);
        Optional<DashboardDTO> dashboardDTO = dashboardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dashboardDTO);
    }

    /**
     * {@code DELETE  /dashboards/:id} : delete the "id" dashboard.
     *
     * @param id the id of the dashboardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDashboard(@PathVariable("id") Long id) {
        log.debug("REST request to delete Dashboard : {}", id);
        dashboardService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
