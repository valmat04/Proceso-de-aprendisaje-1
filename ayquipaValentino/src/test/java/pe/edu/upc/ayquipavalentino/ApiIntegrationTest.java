package pe.edu.upc.ayquipavalentino;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.ayquipavalentino.entities.*;
import pe.edu.upc.ayquipavalentino.repositories.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ApiIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired ZonaRepository zones;
    @Autowired MonitoreoEcologicoRepository monitors;
    @Autowired JwtEncoder encoder;
    @Value("${app.inspector.username}") String username;
    @Value("${app.inspector.password}") String password;

    Map<String,Object> validZone() {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("name", "Sector de restauración"); body.put("location", "Humedal, ribera norte");
        body.put("areaHectares", 12.5); body.put("interventionDate", "14/10/2026");
        body.put("status", "EN RESTAURACIÓN"); return body;
    }
    String token(String role, Instant expiry) {
        JwtClaimsSet claims = JwtClaimsSet.builder().subject("test-user").issuedAt(expiry.minusSeconds(3600))
            .expiresAt(expiry).claim("roles", List.of(role)).build();
        return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
    }
    String inspectorToken() { return token("INSPECTOR", Instant.now().plusSeconds(600)); }

    @Test void registerFiveZonesWithoutTokenReturns201AndPersists() throws Exception {
        for (int i=0; i<5; i++) {
            Map<String,Object> body = validZone(); body.put("name", "Zona coherente " + (i+1));
            String json = mvc.perform(post("/zones").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.idZona").isNumber())
                .andExpect(jsonPath("$.interventionDate").value("14/10/2026"))
                .andReturn().getResponse().getContentAsString();
            Integer id = mapper.readTree(json).get("idZona").intValue();
            assertThat(zones.findById(id).orElseThrow().getName()).isEqualTo(body.get("name"));
        }
    }
    static Stream<Arguments> invalidFields() {
        return Stream.of(Arguments.of("name", " "), Arguments.of("location", ""),
            Arguments.of("status", " "), Arguments.of("areaHectares", -1),
            Arguments.of("areaHectares", 0), Arguments.of("areaHectares", null),
            Arguments.of("interventionDate", null));
    }
    @ParameterizedTest @MethodSource("invalidFields")
    void invalidZonesReturn400WithoutInserting(String field, Object value) throws Exception {
        long before = zones.count(); Map<String,Object> body = validZone(); body.put(field, value);
        mvc.perform(post("/zones").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.fieldErrors." + field).isNotEmpty());
        assertThat(zones.count()).isEqualTo(before);
    }
    @Test void invalidJsonDateReturns400() throws Exception {
        Map<String,Object> body=validZone(); body.put("interventionDate", "fecha-invalida");
        mvc.perform(post("/zones").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest());
    }
    @Test void emptyReportReturnsAnEmptyJsonArray() throws Exception {
        assertThat(monitors.count()).as("Use una BD de pruebas vacía; cada prueba revierte su transacción").isZero();
        mvc.perform(get("/monitors").header("Authorization", "Bearer " + inspectorToken()))
            .andExpect(status().isOk()).andExpect(content().json("[]"));
    }
    @Test void nativeSqlGroupsFiveMonitorsByDate() throws Exception {
        Zona zone = new Zona(); zone.setName("Totoral"); zone.setLocation("Ribera norte"); zone.setAreaHectares(8.0);
        zone.setInterventionDate(LocalDate.of(2026,10,1)); zone.setStatus("EN RESTAURACIÓN"); zones.save(zone);
        for (int i=0;i<5;i++) {
            MonitoreoEcologico m = new MonitoreoEcologico(); m.setZona(zone);
            m.setRecordDate(LocalDate.of(2026,10,12).plusDays(i/2)); m.setWaterQuality(7.0);
            m.setVegetationCoverage(55.0); m.setFaunaCount(15+i); m.setObservations("Monitoreo " + (i+1)); monitors.save(m);
        }
        monitors.flush();
        String response = mvc.perform(get("/monitors").header("Authorization", "Bearer " + inspectorToken()))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(3))).andReturn().getResponse().getContentAsString();
        var json = mapper.readTree(response);
        assertThat(json.get(0).get("date").asText()).isEqualTo("12/10/2026");
        assertThat(json.get(0).get("cantidad ").asLong()).isEqualTo(2);
        assertThat(json.get(1).get("cantidad ").asLong()).isEqualTo(2);
        assertThat(json.get(2).get("cantidad ").asLong()).isEqualTo(1);
    }
    @Test void reportWithoutTokenReturns401() throws Exception {
        mvc.perform(get("/monitors")).andExpect(status().isUnauthorized());
    }
    @Test void invalidTokenReturns401() throws Exception {
        mvc.perform(get("/monitors").header("Authorization", "Bearer invalid-token")).andExpect(status().isUnauthorized());
    }
    @Test void expiredTokenReturns401() throws Exception {
        mvc.perform(get("/monitors").header("Authorization", "Bearer " + token("INSPECTOR", Instant.now().minusSeconds(120))))
            .andExpect(status().isUnauthorized());
    }
    @Test void validTokenWithoutInspectorRoleReturns403() throws Exception {
        mvc.perform(get("/monitors").header("Authorization", "Bearer " + token("USER", Instant.now().plusSeconds(600))))
            .andExpect(status().isForbidden());
    }
    @Test void loginReturnsUsableInspectorToken() throws Exception {
        String response = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("username", username, "password", password))))
            .andExpect(status().isOk()).andExpect(jsonPath("$.tokenType").value("Bearer"))
            .andReturn().getResponse().getContentAsString();
        mvc.perform(get("/monitors").header("Authorization", "Bearer " + mapper.readTree(response).get("accessToken").asText()))
            .andExpect(status().isOk());
    }
    @Test void wrongPasswordReturns401() throws Exception {
        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(Map.of("username", username, "password", UUID.randomUUID().toString()))))
            .andExpect(status().isUnauthorized());
    }
    @Test void openApiAndSwaggerArePublic() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk()).andExpect(jsonPath("$.paths./zones").exists());
        mvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }
}
