package com.sspl.demo.web.rest;

import static com.sspl.demo.domain.DashboardAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sspl.demo.IntegrationTest;
import com.sspl.demo.domain.Dashboard;
import com.sspl.demo.repository.DashboardRepository;
import com.sspl.demo.service.dto.DashboardDTO;
import com.sspl.demo.service.mapper.DashboardMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DashboardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DashboardResourceIT {

    private static final String ENTITY_API_URL = "/api/dashboards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private DashboardMapper dashboardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDashboardMockMvc;

    private Dashboard dashboard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dashboard createEntity(EntityManager em) {
        Dashboard dashboard = new Dashboard();
        return dashboard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dashboard createUpdatedEntity(EntityManager em) {
        Dashboard dashboard = new Dashboard();
        return dashboard;
    }

    @BeforeEach
    public void initTest() {
        dashboard = createEntity(em);
    }

    @Test
    @Transactional
    void createDashboard() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);
        var returnedDashboardDTO = om.readValue(
            restDashboardMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DashboardDTO.class
        );

        // Validate the Dashboard in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDashboard = dashboardMapper.toEntity(returnedDashboardDTO);
        assertDashboardUpdatableFieldsEquals(returnedDashboard, getPersistedDashboard(returnedDashboard));
    }

    @Test
    @Transactional
    void createDashboardWithExistingId() throws Exception {
        // Create the Dashboard with an existing ID
        dashboard.setId(1L);
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDashboards() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dashboard.getId().intValue())));
    }

    @Test
    @Transactional
    void getDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        // Get the dashboard
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL_ID, dashboard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dashboard.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDashboard() throws Exception {
        // Get the dashboard
        restDashboardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void deleteDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dashboard
        restDashboardMockMvc
            .perform(delete(ENTITY_API_URL_ID, dashboard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dashboardRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Dashboard getPersistedDashboard(Dashboard dashboard) {
        return dashboardRepository.findById(dashboard.getId()).orElseThrow();
    }

    protected void assertPersistedDashboardToMatchAllProperties(Dashboard expectedDashboard) {
        assertDashboardAllPropertiesEquals(expectedDashboard, getPersistedDashboard(expectedDashboard));
    }

    protected void assertPersistedDashboardToMatchUpdatableProperties(Dashboard expectedDashboard) {
        assertDashboardAllUpdatablePropertiesEquals(expectedDashboard, getPersistedDashboard(expectedDashboard));
    }
}
