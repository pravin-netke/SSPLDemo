package com.sspl.demo.service.mapper;

import static com.sspl.demo.domain.DashboardAsserts.*;
import static com.sspl.demo.domain.DashboardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DashboardMapperTest {

    private DashboardMapper dashboardMapper;

    @BeforeEach
    void setUp() {
        dashboardMapper = new DashboardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDashboardSample1();
        var actual = dashboardMapper.toEntity(dashboardMapper.toDto(expected));
        assertDashboardAllPropertiesEquals(expected, actual);
    }
}
